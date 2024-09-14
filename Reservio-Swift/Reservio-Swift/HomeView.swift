import SwiftUI

struct HomeView: View {
    let tableName: String

    let availableTimes: [String] = stride(from: 17, to: 24, by: 0.5).map { hour -> String in
        let hours = Int(hour)
        let minutes = hour.truncatingRemainder(dividingBy: 1) * 60
        return String(format: "%02d:%02d", hours, Int(minutes))
    }
    
    @State private var selectedTime: String? = nil
    @State private var customTime = Date()
    @State private var selectedDate = Date()
    @State private var personen: Int = 0
    @State private var sonderWünsche: String = ""
    @State private var kinderStuhl: Bool = false
    @State private var showOrderView = false
    @State private var tableID = ""
    
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

                    Text("Choose Reservation Time")
                        .font(.headline)
                        .foregroundColor(.primary)
                        .padding(.top)

                    // Time selection grid
                    LazyVGrid(columns: [GridItem(.adaptive(minimum: 70), spacing: 15)], spacing: 15) {
                        ForEach(availableTimes, id: \.self) { time in
                            Button(action: {
                                selectedTime = time
                                customTime = stringToDate(time) // Update custom time to match selected time
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

                    // Custom time picker
                    HStack {
                        Text("Custom Time")
                            .font(.subheadline)
                        Spacer()
                        DatePicker("", selection: $customTime, displayedComponents: .hourAndMinute)
                            .onChange(of: customTime) { newCustomTime in
                                // Deselect preselected time only if custom time is different from selected time
                                if selectedTime != nil && formatCustomTime(newCustomTime) != selectedTime {
                                    selectedTime = nil
                                }
                            }
                            .labelsHidden()
                            .background(Color.gray.opacity(0.1))
                            .cornerRadius(10)
                            .frame(width: 100)
                    }
                    .padding(.horizontal, 30)

                    // Table ID input
                    HStack {
                        Text("Table ID")
                            .font(.subheadline)
                        Spacer()
                        TextField("Enter Table ID", text: $tableID)
                            .padding()
                            .background(Color.gray.opacity(0.1))
                            .cornerRadius(10)
                            .frame(width: 150)
                    }
                    .padding(.horizontal, 30)

                    // Date picker for reservation date
                    DatePicker("Reservation Date", selection: $selectedDate, displayedComponents: .date)
                        .padding(.horizontal, 30)

                    // Number of people input
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

                    // Special requests input
                    TextField("Special Requests", text: $sonderWünsche)
                        .padding()
                        .background(Color.gray.opacity(0.1))
                        .cornerRadius(10)
                        .padding(.horizontal, 30)

                    // Toggle for child's chair
                    Toggle("Child’s Chair Required", isOn: $kinderStuhl)
                        .padding(.horizontal, 30)

                    // Confirm reservation button
                    if selectedTime != nil || !formatCustomTime(customTime).isEmpty {
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
                                selectedTime: selectedTime ?? formatCustomTime(customTime),
                                peopleCount: personen,
                                specialRequests: sonderWünsche,
                                kinderStuhl: kinderStuhl,
                                tableID: tableID,
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
    }
    
    // Helper function to convert time string to Date for custom time synchronization
    private func stringToDate(_ time: String) -> Date {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter.date(from: time) ?? Date()
    }

    private func formatCustomTime(_ date: Date) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter.string(from: date)
    }
}

#Preview {
    HomeView(tableName: "Test Table")
}
