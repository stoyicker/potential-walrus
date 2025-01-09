package co.touchlab.spotnotes.note

import co.touchlab.spotnotes.geo.DistanceCalculator
import co.touchlab.spotnotes.geo.LatLong
import co.touchlab.spotnotes.geo.LocationManager
import co.touchlab.spotnotes.randomUUIDString
import dev.jordond.compass.Coordinates
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

/**
 * Use a secondary constructor to allow DI for (hypothetical) testing and general flexibility
 * without exposing internal types as would happen if we just used default values for the parameters
 * in an approach with a public primary constructor instead
 */
class NoteFactory internal constructor(
    private val distanceCalculator: DistanceCalculator,
    private val locationManager: LocationManager,
    private val clock: Clock,
    /**
     * On first sight, it looks like there's an argument to implement location caching logic in
     * [LocationManager] instead. However, it is important to note that the requirement ask about
     * caching location within a minute of notes being saved, not of location lookups. Therefore,
     * implementing the caching of [LocationManager] would lead to unexpected behaviors in future
     * functionality built on top of it.
     */
    private val cachedLatLongFlow: MutableStateFlow<TimestampAndLocation?>,
    private val mutex: Mutex
) {
    constructor() : this(
        DistanceCalculator(),
        LocationManager(Geolocator.mobile()),
        Clock.System,
        MutableStateFlow(null),
        Mutex()
    )

    suspend fun create(note: String, lastCoordinates: LatLong?): Note {
        val newLatLong = when (val cachedLatLong = cachedLatLongFlow.value) {
            null -> updateCachedLatLong()
            else -> if (isEntryStale(cachedLatLong)) {
                updateCachedLatLong()
            } else {
                cachedLatLong.second
            }
        }?.run { latitude to longitude }
        return NoteImpl(
            randomUUIDString(),
            note,
            distanceCalculator.between(lastCoordinates, newLatLong),
            newLatLong
        )
    }

    private fun isEntryStale(timestampAndLocation: TimestampAndLocation) =
        clock.now() - timestampAndLocation.first >= 1.minutes

    /**
     * Location cache updates work by using a double check on the staleness of the cached location.
     * In [create], we do a concurrency-unsafe check. This is not enough by itself for all cases,
     * but it avoids acquiring and releasing the mutex for cases where there is not going to be
     * concurrency competition (which will be most in practice).
     *
     * Then, inside the mutex, we do a new check to see if we actually need a new update, which may
     * be the case if for example the last location request was outstandingly slow.
     *
     * To highlight examples that may be familiar, double checks are how Kotlin's object keyword or
     * Dagger's regular scopes (so, other than @Reusable) work.
     */
    private suspend fun updateCachedLatLong() = mutex.withLock {
        val cachedLatLong = cachedLatLongFlow.value
        if (cachedLatLong == null || isEntryStale(cachedLatLong)) {
            val newLatLong = locationManager.currentOrNull()
            cachedLatLongFlow.value = clock.now() to newLatLong
            newLatLong
        } else {
            cachedLatLong.second
        }
    }
}

private typealias TimestampAndLocation = Pair<Instant, Coordinates?>
