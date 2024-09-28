import Foundation

// Define the Reservation struct to match your JSON structure
struct Reservation: Identifiable, Codable {
    let id: String
    let firstname: String
    let lastname: String
    let date: String
    let peopleCount: Int
    let email: String
    let phoneNumber: String
    let specialRequests: String
    let highChair: Bool
    let tableID: String
    let numberChairs: Int

    // Custom initializer for date conversion
    init(id: String, firstname: String, lastname: String, date: Date, peopleCount: Int, email: String, phoneNumber: String, specialRequests: String, highChair: Bool, tableID: String, numberChairs: Int) {
        self.id = id
        self.firstname = firstname
        self.lastname = lastname
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        self.date = dateFormatter.string(from: date) // Format date to string
        self.peopleCount = peopleCount
        self.email = email
        self.phoneNumber = phoneNumber
        self.specialRequests = specialRequests
        self.highChair = highChair
        self.tableID = tableID
        self.numberChairs = numberChairs
    }
}

// Function to send reservation request
func sendReservationRequest(requestBody: Reservation, ip: String) async {
    print("Sending reservation for: \(requestBody.firstname) \(requestBody.lastname)")

    // Define the server URL
    guard let url = URL(string: "http://\(ip):4567/sendReservation") else {
        print("Error: Invalid URL")
        return
    }

    // Create a URLRequest object
    var request = URLRequest(url: url)
    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
    request.httpMethod = "POST"

    do {
        // Encode the Reservation object to JSON data
        let jsonData = try JSONEncoder().encode(requestBody)

        // Send the request using URLSession
        let (data, response) = try await URLSession.shared.upload(for: request, from: jsonData)

        // Handle the response (optional)
        if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 {
            print("Reservation sent successfully")
        } else {
            print("Error sending reservation: \(String(decoding: data, as: UTF8.self))")
        }
    } catch {
        print("Error sending reservation: \(error)")
    }
}
