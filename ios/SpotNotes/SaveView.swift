//
//  Save.swift
//  SpotNotes
//

import Foundation
import SwiftUI

struct SaveView: View {
    @State private var note: String = ""
    @State private var showAlert = false
    var onSubmit: (String) -> Void
    
    var body: some View {
        
        VStack {
            Text("Save your location with a note!").font(.title2).padding(.vertical)
            TextField("Note", text: $note).textFieldStyle(.roundedBorder)
            Button("Save", action: { onSubmit(note) })
                .buttonStyle(.borderedProminent)
        }
        .padding()
    }
}

#Preview {
    SaveView(onSubmit: {_ in })
}
