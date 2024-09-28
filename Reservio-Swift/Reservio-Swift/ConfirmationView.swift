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
        
        HStack{
            Text("Your Reservations")
                .font(.title)
        }
        .padding(.horizontal)
        VStack {

            if confirmations.isEmpty {
                Text("No Confirmations")
            } else {
                List($confirmations) { $confirmation in
                    Section {
                        HStack {
                            Text("Reservation ID:")
                            Spacer()
                            Text(confirmation.reservation.id)
                        }
                        HStack {
                            Text("Date:")
                            Spacer()
                            Text(formatDate(confirmation.confirmationDate))
                                .foregroundColor(.gray)
                        }
                        HStack {
                            Text("Name:")
                            Spacer()
                            Text("\(confirmation.reservation.firstname) \(confirmation.reservation.lastname)")
                        }
                        HStack {
                            Text("People Count:")
                            Spacer()
                            Text("\(confirmation.reservation.peopleCount)")
                        }
                        HStack {
                            Text("Email:")
                            Spacer()
                            Text(confirmation.reservation.email)
                        }
                        HStack {
                            Text("Phone:")
                            Spacer()
                            Text(confirmation.reservation.phoneNumber)
                        }
                        if !confirmation.reservation.specialRequests.isEmpty {
                            HStack {
                                Text("Special Requests:")
                                Spacer()
                                Text(confirmation.reservation.specialRequests)
                            }
                        }
                        HStack {
                            Text("High Chair:")
                            Spacer()
                            Text(confirmation.reservation.highChair ? "Yes" : "No")
                        }
                        HStack {
                            Text("Table ID:")
                            Spacer()
                            Text(confirmation.reservation.tableID)
                        }
                        HStack {
                            Text("Number of Chairs:")
                            Spacer()
                            Text("\(confirmation.reservation.numberChairs)")
                        }
                    }
                }
                .listStyle(InsetGroupedListStyle())
                .refreshable {
                    confirmations = await fetchConfirmations(firstname: userData.firstname, lastname: userData.lastname)
                }
            }
        }
        .padding()
        .onAppear {
            Task {
                confirmations = await fetchConfirmations(firstname: userData.firstname, lastname: userData.lastname)
            }
        }
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
