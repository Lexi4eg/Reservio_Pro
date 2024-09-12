//
//  SendReservationAPI.swift
//  Reservio-Swift
//
//  Created by Felix Prattes on 12.09.24.
//

import Foundation

var modes = ["Dashboard", "WordClock", "MillionTimes", "MillionBW", "MillionWood", "MillionGlass", "SolarSystem", "FlipDot", ]

func sendReservationRequest(selectedMode: String) async {
    let userData = UserData()
    print("Sending Mode: " + selectedMode)

    guard let selectedModeIndex = modes.enumerated().filter({ $0.element == selectedMode }).first?.offset else {
      print("Selected mode not found in colors array")
      return
    }

    let body = ["mode": selectedModeIndex + 1]
 
     

  guard let url = URL(string: "http://\(userData.ip):3000/api/sendMode") else {
    print("Error: Invalid URL")
    return
  }

  var request = URLRequest(url: url)
  request.setValue("application/json", forHTTPHeaderField: "Content-Type")
  request.httpMethod = "POST"

  do {
    let jsonData = try JSONEncoder().encode(body)

      let (_, _) = try await URLSession.shared.upload(for: request, from: jsonData)

    

    print("Mode sent successfully")
  } catch {
    print("Error sending mode: \(error)")
  }
}
