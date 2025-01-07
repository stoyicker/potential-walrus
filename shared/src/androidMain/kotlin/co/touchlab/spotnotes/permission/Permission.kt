package co.touchlab.spotnotes.permission

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION

actual enum class Permission {
    LOCATION,
    LOCATION_PRECISION_HIGH;

    val asAndroidKey: String
        get() = when (this) {
            LOCATION -> ACCESS_COARSE_LOCATION
            LOCATION_PRECISION_HIGH -> ACCESS_FINE_LOCATION
        }
}
