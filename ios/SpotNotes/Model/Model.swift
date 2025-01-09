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
    private var noteFactory = NoteFactory()

    init(notes: [Note] = []) {
        self.notes = notes
        Task {
            try? await buildPermissionManager().request(permission: Permission.location)
        }
    }

    func saveLocation(note:String) async {

        if let note = try? await noteFactory.create(note: note, lastCoordinates: notes.last?.latLong) {
            notes.append(note)
        }
    }
}

// Used for #Preview macros as I don't want to make NoteImpl public (see doc there)
// Weird place but I cannot use XCode to add a new file to the pbxproj because I have no mac
#if DEBUG
class StubNote: Note {
    let id: String = ""
    let note: String
    let distance: Int64
    let latLong: KotlinPair<KotlinDouble, KotlinDouble>? = nil

    init(note: String, distance: Int64) {
        self.note = note
        self.distance = distance
    }
}
#endif
