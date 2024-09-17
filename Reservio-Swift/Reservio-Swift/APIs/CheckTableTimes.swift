//
//  CheckTableTimes.swift
//  Reservio-Swift
//
//  Created by Felix Prattes on 17.09.24.
//

import Foundation

// Function to check available times for a table reservation
func checkTableTimes(tableId: String, reservationDate: Date) async {
    print("Checking available times for Table ID: \(tableId) on \(reservationDate)")

    // Define the server URL for checking table times
    guard let url = URL(string: "http://localhost:4567/checkTableTimes") else {
        print("Error: Invalid URL")
        return
    }

    // Create the request body with tableId and formatted reservation date
    let requestBody: [String: Any] = [
        "tableId": tableId,
        "reservationDate": ISO8601DateFormatter().string(from: reservationDate)
    ]

    // Create a URLRequest object
    var request = URLRequest(url: url)
    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
    request.httpMethod = "POST"

    do {
        // Encode the request body to JSON data
        let jsonData = try JSONSerialization.data(withJSONObject: requestBody, options: [])

        // Send the request using URLSession
        let (data, response) = try await URLSession.shared.upload(for: request, from: jsonData)

        // Handle the response
        if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 {
            print("Available times received successfully")
            // Process the response data if needed
            if let jsonResponse = try? JSONSerialization.jsonObject(with: data, options: []) {
                print("Response: \(jsonResponse)")
            }
        } else {
            print("Error checking table times: \(String(decoding: data, as: UTF8.self))")
        }
    } catch {
        print("Error sending request: \(error)")
    }
}
