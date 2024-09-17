//
//  ContentView.swift
//  Reservio-Swift
//
//  Created by Felix Prattes on 12.09.24.
//

import SwiftUI
import SwiftData

struct ContentView: View {
    @Environment(\.modelContext) private var modelContext
    @Query private var items: [Item]
    
    @StateObject private var userData = UserData()


    var body: some View {
                TabView {
                    Tab("Home", systemImage: "house"){
                        HomeView()
                    }
                   
                    Tab("Profile", systemImage: "gear"){
                        SettingsView()
                    }
                }
                .foregroundColor(.white)
                .accentColor(.white)
                .tabViewStyle(.sidebarAdaptable)
    }

    private func addItem() {
        withAnimation {
            let newItem = Item(timestamp: Date())
            modelContext.insert(newItem)
        }
    }

    private func deleteItems(offsets: IndexSet) {
        withAnimation {
            for index in offsets {
                modelContext.delete(items[index])
            }
        }
    }
}

#Preview {
    ContentView()
        .modelContainer(for: Item.self, inMemory: true)
}
