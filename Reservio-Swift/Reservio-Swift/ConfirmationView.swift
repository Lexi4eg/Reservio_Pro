//
//  ConfirmationView.swift
//  Reservio-Swift
//
//  Created by Felix Prattes on 26.09.24.
//

import SwiftUI

struct Confirmation: Codable, Identifiable {
    var id: String
    var confirmationDate: Date
    var confirmationNumber: String
    var reservation: Reservation
}

struct ConfirmationView: View {
    @State private var confirmations: [Confirmation] = []
    @ObservedObject var userData: UserData

    var body: some View {
        VStack {
            TextField("First Name", text: $userData.firstname)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()

            TextField("Last Name", text: $userData.lastname)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()

            Button("Fetch Confirmations") {
                Task {
                    confirmations = await fetchConfirmations(firstname: userData.firstname, lastname: userData.lastname)
                }
            }
            .padding()

            if confirmations.isEmpty {
                Text("No Confirmations")
            } else {
                List($confirmations) { $confirmation in
                    VStack(alignment: .leading) {
                        Text("Confirmation Number: \(confirmation.confirmationNumber)")
                            .font(.headline)
                        Text("ID: \(confirmation.id)")
                        Text("Date: \(formatDate(confirmation.confirmationDate))")
                            .foregroundColor(.gray)
                        Text("Reservation ID: \(confirmation.reservation.id)")
                        Text("Name: \(confirmation.reservation.firstname) \(confirmation.reservation.lastname)")
                        Text("People Count: \(confirmation.reservation.peopleCount)")
                        Text("Email: \(confirmation.reservation.email)")
                        Text("Phone: \(confirmation.reservation.phoneNumber)")
                        if !confirmation.reservation.specialRequests.isEmpty {
                            Text("Special Requests: \(confirmation.reservation.specialRequests)")
                        }
                        Text("High Chair: \(confirmation.reservation.highChair ? "Yes" : "No")")
                        Text("Table ID: \(confirmation.reservation.tableID)")
                        Text("Number of Chairs: \(confirmation.reservation.numberChairs)")
                    }
                    .padding()
                }
            }
        }
        .padding()
    }

    private func formatDate(_ date: Date) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .short
        dateFormatter.timeStyle = .none
        return dateFormatter.string(from: date)
    }
}

#Preview {
    ConfirmationView(userData: UserData())
}
