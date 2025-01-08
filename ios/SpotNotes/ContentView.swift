//
//  ContentView.swift
//  SpotNotes
//

import Shared
import SwiftUI

struct ContentView: View {
    @EnvironmentObject private var model: Model
    @State private var selectedTab: Int = 0
    
    var body: some View {
        TabView(selection: $selectedTab){
            SaveView(onSubmit: saveNote).tabItem {
                Label("Save", systemImage: "square.and.pencil")
            }.tag(0)
            HistoryView().tabItem {
                Label("History", systemImage: "clock.fill")
            }.tag(1)
        }
    }
}

private extension ContentView{
    func saveNote(note: String){
        Task{
            await model.saveLocation(note: note)
        }
        selectedTab = 1
    }
}

#Preview {
    ContentView()
        .environmentObject(Model(notes: [buildNote(note: "Test1", distance: 500, latLong: nil), buildNote(note: "Test2", distance: 1000, latLong: nil)]))
}
