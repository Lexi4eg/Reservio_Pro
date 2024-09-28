//
//  fetchConfirmations.swift
//  Reservio-Swift
//
//  Created by Felix Prattes on 27.09.24.
//

import Foundation


func fetchConfirmations(firstname: String, lastname: String, ip:String) async -> [Confirmation] {
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
            let decoder = JSONDecoder()
            if let confirmations = try? decoder.decode([Confirmation].self, from: data) {
                return confirmations
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
