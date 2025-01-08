package co.touchlab.spotnotes

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sin
import kotlin.math.sqrt

class DistanceCalculator {
    fun between(latLongA: Pair<Double, Double>?, latLongB: Pair<Double, Double>?): Long {
        if (latLongA == null || latLongB == null) {
            return 0
        }
        val (lat1, lon1) = latLongA
        val (lat2, lon2) = latLongB
        val latARad = lat1.toRadians
        val lonARad = lon1.toRadians
        val latBRad = lat2.toRadians
        val lonBRad = lon2.toRadians

        val dLat = latBRad - latARad
        val dLon = lonBRad - lonARad
        val a = sin(dLat / 2).pow(2) + cos(latARad) * cos(latBRad) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (EARTH_RADIUS_METERS * c).roundToLong()
    }

    private val Double.toRadians: Double
        get() = this * (PI / 180)

    companion object {
        private const val EARTH_RADIUS_METERS = 6_371_000
    }
}
