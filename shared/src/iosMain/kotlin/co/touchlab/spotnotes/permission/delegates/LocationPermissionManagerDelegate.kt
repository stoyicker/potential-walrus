package co.touchlab.spotnotes.permission.delegates

import co.touchlab.spotnotes.permission.RequestResult
import kotlin.coroutines.suspendCoroutine
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.darwin.NSObject
import platform.Foundation.NSError
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

internal class LocationPermissionManagerDelegate : PermissionManagerDelegate {
    override suspend fun invoke(): RequestResult =
        when (CLLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedAlways,
            kCLAuthorizationStatusAuthorizedWhenInUse -> RequestResult.ALREADY_GRANTED

            kCLAuthorizationStatusNotDetermined -> {
                val newStatus = suspendCoroutine<CLAuthorizationStatus> { continuation ->
                    LocationManagerCallbackBridge(continuation).requestAuthorization()
                }
                when (newStatus) {
                    kCLAuthorizationStatusAuthorizedAlways,
                    kCLAuthorizationStatusAuthorizedWhenInUse -> RequestResult.NEWLY_GRANTED

                    kCLAuthorizationStatusDenied -> RequestResult.NOT_GRANTED
                    else -> RequestResult.UNKNOWN
                }
            }

            kCLAuthorizationStatusDenied -> RequestResult.REQUEST_PREVENTED
            else -> RequestResult.UNKNOWN
        }

    class LocationManagerCallbackBridge(continuation: Continuation<CLAuthorizationStatus>) :
        NSObject(), CLLocationManagerDelegateProtocol {
        private var continuation: Continuation<CLAuthorizationStatus>? = continuation
        private val locationManager = CLLocationManager()

        init {
            locationManager.delegate = this
        }

        fun requestAuthorization() {
            locationManager.requestWhenInUseAuthorization()
        }

        override fun locationManager(
            manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus
        ) {
            continuation!!.resume(didChangeAuthorizationStatus)
            continuation = null
        }
    }
}
