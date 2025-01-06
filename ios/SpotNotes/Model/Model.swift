//
//  Model.swift
//  SpotNotes
//

import Foundation
import CoreLocation
import Shared

@MainActor
class Model: ObservableObject{
    
    @Published var notes:[SpotNote]
    private var locationManager = LocationManager()
    
    init(notes: [SpotNote] = []) {
        self.notes = notes
    }
    
    func saveLocation(note:String) async throws{

        let newLocation = try await locationManager.requestLocation()
        let distance = calculateDistance(newLocation: newLocation, oldLocation: notes.last?.location)
        notes.append(SpotNote(note: note, distance: distance, location: newLocation))
    }
    
    func calculateDistance(newLocation: CLLocation?, oldLocation: CLLocation?) -> CLLocationDistance {
        guard let newLocation = newLocation, let oldLocation = oldLocation else {
            return 0
        }
        return newLocation.distance(from: oldLocation)
    }
}
