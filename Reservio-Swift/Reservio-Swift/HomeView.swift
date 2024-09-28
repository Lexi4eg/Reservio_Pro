import SwiftUI

struct HomeView: View {
    let availableTimes: [String] = stride(from: 17, to: 24, by: 0.5).map { hour -> String in
        let hours = Int(hour)
        let minutes = hour.truncatingRemainder(dividingBy: 1) * 60
        return String(format: "%02d:%02d", hours, Int(minutes))
    }

    let tableIDs = ["T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8"]

    @State private var selectedTime: String? = nil
    @State private var selectedDate = Date()
    @State private var personen: Int = 0
    @State private var sonderWünsche: String = ""
    @State private var kinderStuhl: Bool = false
    @State private var showOrderView = false
    @State private var tableID = ""
    @State private var numberChais = 0
    @State private var bookedTableIDs: [String] = []
    @State private var isFetchingTimes = false
    @State private var errorMessage = ""
    @ObservedObject var userData: UserData


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
                                .disabled(bookedTableIDs.contains(id))
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
                                get: { String(numberChais) },
                                set: { newValue in
                                    if let value = Int(newValue) {
                                        numberChais = value
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
                                date: combineDateAndTime(date: selectedDate, time: selectedTime ?? ""),
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

    private func triggerAPICallIfNeeded() {
        guard selectedTime != nil && !selectedTime!.isEmpty else { return }

        isFetchingTimes = true
        Task {
          
            let bookedTables =  await checkTableTimes(reservationDate:combineDateAndTime(date: selectedDate, time: selectedTime ?? ""), ip: userData.ip)
            bookedTableIDs = bookedTables
            isFetchingTimes = false
        }
    }

    private func combineDateAndTime(date: Date, time: String) -> Date {
        let calendar = Calendar.current
        let timeComponents = time.split(separator: ":").map { Int($0) ?? 0 }
        var dateComponents = calendar.dateComponents([.year, .month, .day], from: date)
        dateComponents.hour = timeComponents[0]
        dateComponents.minute = timeComponents[1]
        return calendar.date(from: dateComponents) ?? date
    }
}

#Preview {
    HomeView(userData: UserData())
}
