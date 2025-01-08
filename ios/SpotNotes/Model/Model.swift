//
//  Model.swift
//  SpotNotes
//

import Foundation
import CoreLocation
import Shared

@MainActor
class Model: ObservableObject{
    
    @Published var notes:[Note]
    private var locationManager = LocationManager()
    private let distanceCalculator = DistanceCalculator()
    
    init(notes: [Note] = []) {
        self.notes = notes
        Task {
            try? await buildPermissionManager().request(permission: Permission.location)
        }
    }
    
    func saveLocation(note:String) async {

        let newLocation = try? await locationManager.requestLocation()?.coordinate
        var newLatLong: KotlinPair<KotlinDouble, KotlinDouble>?
        if let safeNewLocation = newLocation {
            newLatLong = KotlinPair(
                first: KotlinDouble(value: safeNewLocation.latitude),
                second: KotlinDouble(value: safeNewLocation.longitude)
            )
        }
        let distance = distanceCalculator.between(
            latLongA: newLatLong, latLongB: notes.last?.latLong
        )
        notes.append(buildNote(note: note, distance: distance, latLong: newLatLong))
    }
}
