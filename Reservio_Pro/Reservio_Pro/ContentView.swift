import SwiftUI

import Foundation

struct Reservation: Identifiable {
    let id: UUID
    let firstname: String
    let lastname: String
    let date: Date
    let peopleCount: Int
    let email: String
    let phoneNumber: String
    let specialRequests: String
    let highChair: Bool
    let tableID: String
    let numberChairs: Int
}



struct ContentView: View {
    @State private var reservations: [Reservation] = [
        // Sample data
        Reservation(id: UUID(), firstname: "John", lastname: "Doe", date: Date(), peopleCount: 4, email: "john.doe@example.com", phoneNumber: "123-456-7890", specialRequests: "None", highChair: false, tableID: "A1", numberChairs: 4),
        Reservation(id: UUID(), firstname: "Jane", lastname: "Smith", date: Date(), peopleCount: 2, email: "jane.smith@example.com", phoneNumber: "098-765-4321", specialRequests: "Vegan", highChair: true, tableID: "B2", numberChairs: 2)
    ]
    @State private var searchText = ""
    @State private var isEditing = false

    var filteredReservations: [Reservation] {
        if searchText.isEmpty {
            return reservations
        } else {
            return reservations.filter { $0.firstname.contains(searchText) || $0.lastname.contains(searchText) }
        }
    }

    var body: some View {
        
            List {
                ForEach(filteredReservations) { reservation in
                    VStack(alignment: .leading) {
                        Text("\(reservation.firstname) \(reservation.lastname)")
                            .font(.headline)
                        Text("Date: \(reservation.date, formatter: dateFormatter)")
                        Text("People: \(reservation.peopleCount)")
                        Text("Email: \(reservation.email)")
                        Text("Phone: \(reservation.phoneNumber)")
                        Text("Special Requests: \(reservation.specialRequests)")
                        Text("High Chair: \(reservation.highChair ? "Yes" : "No")")
                        Text("Table ID: \(reservation.tableID)")
                        Text("Number of Chairs: \(reservation.numberChairs)")
                    }
                }
                .onDelete(perform: isEditing ? deleteReservation : nil)
            }
            .searchable(text: $searchText)
            .navigationTitle("Reservations")
            .toolbar {
                ToolbarItem(placement: .automatic) {
                    Button(action: {
                        isEditing.toggle()
                    }) {
                        Text(isEditing ? "Done" : "Edit")
                    }
                }
            
        }
    }

    private func deleteReservation(at offsets: IndexSet) {
        reservations.remove(atOffsets: offsets)
    }

    private var dateFormatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .short
        return formatter
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
