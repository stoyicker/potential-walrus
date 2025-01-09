package co.touchlab.spotnotes.note

import co.touchlab.spotnotes.geo.DistanceCalculator
import co.touchlab.spotnotes.geo.LatLong
import co.touchlab.spotnotes.geo.LocationManager
import co.touchlab.spotnotes.randomUUIDString
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile

/**
 * This class is the entry point to the shared code. It uses a secondary constructor to allow DI
 * for testing and general flexibility without exposing internal types as would happen if we just
 * used default values for the parameters in an approach with a public primary constructor instead
 */
class NoteFactory private constructor(
    private val distanceCalculator: DistanceCalculator,
    private val locationManager: LocationManager
) {
    constructor() : this(DistanceCalculator(), LocationManager(Geolocator.mobile()))

    suspend fun create(note: String, lastCoordinates: LatLong?): Note =
        locationManager.currentOrNull()
            ?.run { latitude to longitude }.let {
                NoteImpl(
                    randomUUIDString(),
                    note,
                    distanceCalculator.between(lastCoordinates, it),
                    it
                )
            }
}
