import SwiftUI

struct HomeView: View {
    let availableTimes: [String] = stride(from: 17, to: 24, by: 0.5).map { hour -> String in
        let hours = Int(hour)
        let minutes = hour.truncatingRemainder(dividingBy: 1) * 60
        return String(format: "%02d:%02d", hours, Int(minutes))
    }
    
    let tableIDs = ["T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8"] // Sample table IDs
    
    @State private var selectedTime: String? = nil
    @State private var selectedDate = Date()
    @State private var personen: Int = 0
    @State private var sonderWünsche: String = ""
    @State private var kinderStuhl: Bool = false
    @State private var showOrderView = false
    @State private var tableID = ""
    @State private var numberChais = 0
    @State private var bookedTableIDs: [String] = [] // List of booked table IDs

    @State private var isFetchingTimes = false
    @State private var errorMessage = ""

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 20) {
                    Image("download")
                        .resizable()
                        .scaledToFill()
                        .frame(width: 250, height: 150)
                        .cornerRadius(15)
                        .shadow(radius: 5)

                    // Date picker for reservation date (no past dates allowed)
                    DatePicker("Reservation Date", selection: $selectedDate, in: Date()..., displayedComponents: .date)
                        .padding(.horizontal, 30)

                    Text("Choose Reservation Time")
                        .font(.headline)
                        .foregroundColor(.primary)
                        .padding(.top)

                    LazyVGrid(columns: [GridItem(.adaptive(minimum: 70), spacing: 15)], spacing: 15) {
                        ForEach(availableTimes, id: \.self) { time in
                            Button(action: {
                                selectedTime = time
                                triggerAPICallIfNeeded() // Check for both time and date
                            }) {
                                Text(time)
                                    .font(.subheadline)
                                    .frame(width: 70, height: 40)
                                    .background(selectedTime == time ? Color.blue : Color.gray.opacity(0.2))
                                    .foregroundColor(selectedTime == time ? .white : .primary)
                                    .cornerRadius(10)
                                    .shadow(radius: selectedTime == time ? 3 : 0)
                            }
                        }
                    }
                    .padding(.horizontal, 30)

                    if isFetchingTimes {
                        ProgressView("Fetching available tables...")
                            .padding()
                    } else {
                        Text("Choose Table ID")
                            .font(.headline)
                        LazyVGrid(columns: [GridItem(.adaptive(minimum: 70), spacing: 15)], spacing: 15) {
                            ForEach(tableIDs, id: \.self) { id in
                                Button(action: {
                                    tableID = id
                                }) {
                                    Text(id)
                                        .font(.subheadline)
                                        .frame(width: 70, height: 40)
                                        .background(tableID == id ? Color.green : Color.gray.opacity(0.2))
                                        .foregroundColor(bookedTableIDs.contains(id) ? .gray : (tableID == id ? .white : .primary))
                                        .cornerRadius(10)
                                        .shadow(radius: tableID == id ? 3 : 0)
                                }
                                .disabled(bookedTableIDs.contains(id)) // Disable booked table IDs
                            }
                        }
                        .padding(.horizontal, 30)
                    }
                    
                    HStack {
                        Text("Number of People")
                            .font(.subheadline)
                        Spacer()
                        TextField("Enter Number", text: Binding(
                            get: { String(personen) },
                            set: { newValue in
                                if let value = Int(newValue) {
                                    personen = value
                                }
                            }
                        ))
                        .keyboardType(.numberPad)
                        .padding()
                        .background(Color.gray.opacity(0.1))
                        .cornerRadius(10)
                        .frame(width: 100)
                    }
                    .padding(.horizontal, 30)
                    
                    
                    Toggle("Child’s Chair Required", isOn: $kinderStuhl)
                        .padding(.horizontal, 30)
                    
                    if(kinderStuhl){
                        HStack {
                            Text("Number of chairs needed")
                                .font(.subheadline)
                            Spacer()
                            TextField("Enter Number", text: Binding(
                                get: { String(personen) },
                                set: { newValue in
                                    if let value = Int(newValue) {
                                        personen = value
                                    }
                                }
                            ))
                            .keyboardType(.numberPad)
                            .padding()
                            .background(Color.gray.opacity(0.1))
                            .cornerRadius(10)
                            .frame(width: 100)
                        }
                        .padding(.horizontal, 30)
                    }
                    
                    TextField("Special Requests", text: $sonderWünsche)
                        .padding()
                        .background(Color.gray.opacity(0.1))
                        .cornerRadius(10)
                        .padding(.horizontal, 30)
                    
                    if selectedTime != nil {
                                            Button(action: {
                                                showOrderView = true
                                            }) {
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
                                            .background(
                                                NavigationLink(destination: OrderView(
                                                    date: selectedDate,
                                                    selectedTime: selectedTime ?? "",
                                                    peopleCount: personen,
                                                    specialRequests: sonderWünsche,
                                                    kinderStuhl: kinderStuhl,
                                                    tableID: tableID,
                                                    numberChais: numberChais,
                                                    userData: UserData()), isActive: $showOrderView) {
                                                    EmptyView()
                                                }
                                                .hidden()
                                            )
                                        }

                                        Spacer()
                                    }
                                }
                                .navigationBarTitle("Home", displayMode: .inline)
                                .background(Color(.systemGroupedBackground))
                            }
                            .alert(isPresented: .constant(!errorMessage.isEmpty), content: {
                                Alert(title: Text("Error"), message: Text(errorMessage), dismissButton: .default(Text("OK")))
                            })
                        }
                        
                        // Trigger the API call if both table ID and time are selected
                        private func triggerAPICallIfNeeded() {
                            guard selectedTime != nil && !selectedTime!.isEmpty else { return }

                            Task {
                                await checkTableTimes()
                            }
                        }

                        // API call function
                        private func checkTableTimes() async {
                            isFetchingTimes = true
                            errorMessage = ""
                            
                            guard let url = URL(string: "http://localhost:4567/checkTableTimes") else {
                                errorMessage = "Invalid URL"
                                isFetchingTimes = false
                                return
                            }

                            let requestBody: [String: Any] = [
                                "reservationDate": ISO8601DateFormatter().string(from: selectedDate),
                                "reservationTime": selectedTime ?? ""
                            ]

                            var request = URLRequest(url: url)
                            request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                            request.httpMethod = "POST"

                            do {
                                let jsonData = try JSONSerialization.data(withJSONObject: requestBody, options: [])
                                let (data, response) = try await URLSession.shared.upload(for: request, from: jsonData)

                                if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 {
                                    // Decode the response to get the booked table IDs
                                    if let jsonResponse = try? JSONSerialization.jsonObject(with: data, options: []) as? [String: Any],
                                       let bookedTables = jsonResponse["bookedTableIDs"] as? [String] {
                                        DispatchQueue.main.async {
                                            bookedTableIDs = bookedTables
                                            isFetchingTimes = false
                                        }
                                    }
                                } else {
                                    errorMessage = "Error fetching available tables"
                                    isFetchingTimes = false
                                }
                            } catch {
                                errorMessage = "Failed to send request: \(error)"
                                isFetchingTimes = false
                            }
                        }
                    }

                    #Preview {
                        HomeView()
                    }
