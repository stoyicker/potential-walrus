//
//  History.swift
//  SpotNotes
//

import Foundation
import SwiftUI


struct HistoryView: View {
    @EnvironmentObject private var model: Model
    
    var body: some View {
        List(model.notes){ entry in
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
        .environmentObject(Model(notes: [SpotNote(note: "Test1", distance: 500, location: nil), SpotNote(note: "Test2", distance: 1000, location: nil)]))
}
