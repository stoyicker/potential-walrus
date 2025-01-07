
import CoreLocation
import Shared

class LocationManager: NSObject, CLLocationManagerDelegate {
    let manager = CLLocationManager()
    private var locationContinuation: CheckedContinuation<CLLocation?, Error>?
    
    override init() {
        super.init()
        manager.delegate = self
        Task {
            try? await buildPermissionManager().request(permission: Permission.location)
        }
    }
    
    func requestLocation() async throws -> CLLocation? {
        return try await withCheckedThrowingContinuation{ continuation in
            locationContinuation = continuation
            if(CLLocationManager.locationServicesEnabled()){
                manager.requestLocation()
            } else {
                locationContinuation?.resume(throwing: LocationError.servicesDisabled)
                locationContinuation = nil
            }
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        locationContinuation?.resume(throwing: error)
        locationContinuation = nil
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]){
        locationContinuation?.resume(returning: locations.first)
        locationContinuation = nil
    }

    enum LocationError: Error {
        case servicesDisabled
    }
}
