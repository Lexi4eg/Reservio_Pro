//
//  ConfirmationView.swift
//  Reservio-Swift
//
//  Created by Felix Prattes on 26.09.24.
//

import SwiftUI

struct Confirmation: Codable {
    var id: String
    var confirmationDate: String
    var confirmationNumber: String
    var reservation: Reservation
}

struct ConfirmationView: View {
    @State private var confirmation: Confirmation?
    @State private var firstname: String = ""
    @State private var lastname: String = ""

    var body: some View {
        VStack {
        

            Button("Fetch Confirmation"){
                 fetchConfirmations(firstname: firstname, lastname: lastname)
            }
            .padding()

            if let confirmation = confirmation {
                Text("Confirmation Received")
                Text("ID: \(confirmation.id)")
                Text("Confirmation Number: \(confirmation.confirmationNumber)")
                Text("Reservation ID: \(confirmation.reservation.id)")
            } else {
                Text("No Confirmation")
            }
        }
        .padding()
    }
    
    private func triggerAPICallIfNeeded() {
        Task {
            do {
                let confirmationsRaw = try await fetchConfirmations(firstname: "Felix", lastname: "Test123")
                confirmation = confirmationsRaw
            } catch {
                errorMessage = "Failed to fetch available tables"
            }
            isFetchingTimes = false
        }
        
    }
    

}

#Preview {
    ConfirmationView()
}
