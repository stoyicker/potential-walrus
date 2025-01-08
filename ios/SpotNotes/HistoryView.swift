//
//  History.swift
//  SpotNotes
//

import Foundation
import Shared
import SwiftUI


struct HistoryView: View {
    @EnvironmentObject private var model: Model
    
    var body: some View {
        List(model.notes, id: \.id){ (entry: Note) in
                HStack{
                    Text(entry.note)
                    Spacer()
                    Text("\(entry.distance, specifier: "%.0f") meters")
                }
            }.listStyle(.plain)
    }
    
    
}

#Preview {
    HistoryView()
        .environmentObject(Model(notes: [buildNote(note: "Test1", distance: 500, latLong: nil), buildNote(note: "Test2", distance: 1000, latLong: nil)]))
}
