import Foundation

func checkTableTimes(reservationDate: Date, ip:String) async -> [String] {
    let dateFormatter = ISO8601DateFormatter()
    dateFormatter.formatOptions = [.withInternetDateTime, .withFractionalSeconds]
    let formattedDate = dateFormatter.string(from: reservationDate)
    
    print("Checking available times on \(formattedDate)")

    // Define the server URL for checking table times with query parameters
    guard var urlComponents = URLComponents(string: "http://\(ip):4567/getTablesByTime") else {
        print("Error: Invalid URL")
        return []
    }
    
    urlComponents.queryItems = [
        URLQueryItem(name: "date", value: formattedDate)
    ]
    
    guard let url = urlComponents.url else {
        print("Error: Invalid URL components")
        return []
    }

    // Create a URLRequest object
    var request = URLRequest(url: url)
    request.httpMethod = "GET"

    do {
        let (data, response) = try await URLSession.shared.data(for: request)

        if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 {
            print("Available times received successfully")
            if let jsonResponse = try? JSONSerialization.jsonObject(with: data, options: []) as? [String] {
                return jsonResponse
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
