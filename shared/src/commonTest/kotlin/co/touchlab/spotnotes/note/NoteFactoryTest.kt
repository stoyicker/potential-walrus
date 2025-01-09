package co.touchlab.spotnotes.note

import co.touchlab.spotnotes.geo.DistanceCalculator
import co.touchlab.spotnotes.geo.LocationManager
import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.LocationRequest
import dev.jordond.compass.geolocation.TrackingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * This is a sample test showcasing how respecting DI enables testing
 */
internal class NoteFactoryTest {
    private val geolocator = object : Geolocator {
        var currentCallCounter = 0

        override suspend fun current(priority: Priority) = GeolocatorResult.NotFound.also {
            currentCallCounter++
        }

        override suspend fun isAvailable() = false

        override fun stopTracking() = Unit

        override fun track(request: LocationRequest) = emptyFlow<TrackingStatus>()

        override val trackingStatus = emptyFlow<TrackingStatus>()
    }
    private val clock = object : Clock {
        lateinit var nowStub: Instant

        override fun now() = nowStub
    }
    private val subject = NoteFactory(
        DistanceCalculator(),
        LocationManager(geolocator),
        clock,
        MutableStateFlow(null),
        Mutex()
    )

    @Test
    fun `should only get location once`() = runTest {
        clock.nowStub = Instant.fromEpochMilliseconds(3)
        subject.create("first", null)
        clock.nowStub += 14.seconds
        subject.create("second", null)
        assertEquals(1, geolocator.currentCallCounter)
    }

    @Test
    fun `should get location twice`() = runTest {
        clock.nowStub = Instant.fromEpochMilliseconds(3)
        subject.create("first", null)
        clock.nowStub += 2.minutes
        subject.create("second", null)
        assertEquals(2, geolocator.currentCallCounter)
    }
}
