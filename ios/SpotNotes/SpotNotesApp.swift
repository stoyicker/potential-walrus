//
//  SpotNotesApp.swift
//  SpotNotes
//

import SwiftUI

@main
struct SpotNotesApp: App {
    @StateObject private var model = Model()
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(model)
        }
    }
}
