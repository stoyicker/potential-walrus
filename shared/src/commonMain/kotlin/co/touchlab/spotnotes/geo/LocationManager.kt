package co.touchlab.spotnotes.geo

import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult

internal class LocationManager(private val geolocator: Geolocator) {
    suspend fun currentOrNull() = (geolocator.current(priority = Priority.HighAccuracy)
        .takeIf { it is GeolocatorResult.Success }
            as? GeolocatorResult.Success)
        ?.data
        ?.coordinates
}
