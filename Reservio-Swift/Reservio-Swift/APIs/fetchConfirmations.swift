import Foundation

func fetchConfirmations(firstname: String, lastname: String, ip: String) async -> [Confirmation] {
    guard var urlComponents = URLComponents(string: "http://\(ip):4567/getConfirmationsByName") else {
        print("Error: Invalid URL")
        return []
    }

    urlComponents.queryItems = [
        URLQueryItem(name: "firstname", value: firstname),
        URLQueryItem(name: "lastname", value: lastname)
    ]

    guard let url = urlComponents.url else {
        print("Error: Invalid URL components")
        return []
    }

    var request = URLRequest(url: url)
    print("Request url", url)
    request.httpMethod = "GET"

    do {
        let (data, response) = try await URLSession.shared.data(for: request)

        if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 {
            print("Available Confirmations received successfully")
            print("JSON Response: \(String(data: data, encoding: .utf8) ?? "Invalid JSON")")

            let decoder = JSONDecoder()
            decoder.dateDecodingStrategy = .custom { decoder in
                let container = try decoder.singleValueContainer()
                let timestamp = try container.decode(Int.self)
                
                // Convert the timestamp from milliseconds to seconds
                let date = Date(timeIntervalSince1970: TimeInterval(timestamp) / 1000)
                return date
            }

            if var confirmations = try? decoder.decode([Confirmation].self, from: data) {
                let formatter = DateFormatter()
                formatter.dateStyle = .medium
                formatter.timeStyle = .short
                
                // Format the date for each confirmation
                confirmations = confirmations.map { confirmation in
                    var updatedConfirmation = confirmation
                    updatedConfirmation.reservation.dateString = formatter.string(from: updatedConfirmation.reservation.date)
                    return updatedConfirmation
                }

                return Array(confirmations.prefix(20))
            } else {
                print("Error parsing JSON response")
                return []
            }
        } else {
            print("Error checking table times: \(String(decoding: data, as: UTF8.self))")
            return []
        }
    } catch {
        print("Error sending request: \(error)")
        return []
    }
}
