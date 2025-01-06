//
//  SpotNote.swift
//  SpotNotes
//

import Foundation
import CoreLocation

struct SpotNote: Identifiable {
    var id = UUID()
    var note: String
    var distance: Double
    var location: CLLocation?
}
