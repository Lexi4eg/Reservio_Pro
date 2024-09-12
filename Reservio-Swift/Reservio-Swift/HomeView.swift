//
//  HomeView.swift
//  Reservio-Swift
//
//  Created by Felix Prattes on 12.09.24.
//


import SwiftUI

struct HomeView: View {
    let tableName: String
    let availableTimes: [String] = ["18:00", "19:00", "20:00"]
    @State private var selectedTime: String?
    @State private var customTime: String = ""
    @State private var selectedDate = Date()
    @State private var personen: String = ""
    @State private var sonderWünsche: String = ""
    @State private var kinderStuhl: Bool = false
    
    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Image("download")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 250, height: 150)
                    .cornerRadius(15)
                    .shadow(radius: 5)
                
                Text("Tisch 1")
                    .font(.title)
                    .fontWeight(.bold)
                
                Text("Select a time")
                    .font(.headline)
                    .foregroundColor(.gray)
                
                // Vorhandene Zeiten
                HStack(spacing: 15) {
                    ForEach(availableTimes, id: \.self) { time in
                        Button(action: {
                            selectedTime = time
                        }) {
                            Text(time)
                                .font(.headline)
                                .frame(width: 60, height: 40)
                                .background(selectedTime == time ? Color.blue : Color.gray.opacity(0.2))
                                .foregroundColor(selectedTime == time ? .white : .primary)
                                .cornerRadius(10)
                                .shadow(radius: selectedTime == time ? 3 : 0)
                        }
                    }
                }
                
                // Benutzerdefinierte Zeit
                TextField("Benutzerdefinierte Zeit", text: $customTime)
                    .padding()
                    .background(Color.gray.opacity(0.1))
                    .cornerRadius(10)
                    .padding(.horizontal, 30)

                // Datumsauswahl
                DatePicker("Wähle ein Datum", selection: $selectedDate, displayedComponents: .date)
                    .padding(.horizontal, 30)

                // Personenanzahl
                TextField("Personenanzahl", text: $personen)
                    .keyboardType(.numberPad)
                    .padding()
                    .background(Color.gray.opacity(0.1))
                    .cornerRadius(10)
                    .padding(.horizontal, 30)
                
                // Sonderwünsche
                TextField("Sonderwünsche", text: $sonderWünsche)
                    .padding()
                    .background(Color.gray.opacity(0.1))
                    .cornerRadius(10)
                    .padding(.horizontal, 30)
                
                // Kinderstuhl Option
                Toggle("Kinderstuhl benötigt", isOn: $kinderStuhl)
                    .padding(.horizontal, 30)
                
                // Reservierung bestätigen
                if selectedTime != nil || !customTime.isEmpty {
                    NavigationLink(destination: OrderView()) {
                        Text("Review Reservation")
                            .font(.headline)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color.blue)
                            .foregroundColor(.white)
                            .cornerRadius(10)
                    }
                    .padding(.top, 20)
                    .padding(.horizontal, 30)
                }
                
                Spacer()
            }
        }
    }
}

#Preview {
    HomeView(tableName: "test")
}

