import SwiftUI

struct HomeView: View {
    let tableName: String
    let availableTimes: [String] = ["18:00", "19:00", "20:00"]
    @State private var selectedTime: String?
    @State private var customTime = Date() // Changed to Date for DatePicker
    @State private var selectedDate = Date()
    @State private var personen: Int = 0
    @State private var sonderWünsche: String = ""
    @State private var kinderStuhl: Bool = false
    @State private var showOrderView = false

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
                    
                    Text("Tisch 1")
                        .font(.title)
                        .fontWeight(.bold)
                    
                    Text("Select a time")
                        .font(.headline)
                        .foregroundColor(.gray)
                    
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
                    
                    HStack {
                        Text("Benutzerdefinierte Zeit")
                        Spacer()
                        DatePicker(
                            "Benutzerdefinierte Zeit",
                            selection: $customTime,
                            displayedComponents: .hourAndMinute
                        )
                        .labelsHidden()
                        .background(Color.gray.opacity(0.1))
                        .cornerRadius(10)
                        
                    }
                    .padding(.horizontal, 30)
                    
                    DatePicker("Wähle ein Datum", selection: $selectedDate, displayedComponents: .date)
                        .padding(.horizontal, 30)
                    
                    TextField("Personenanzahl", text: Binding(
                        get: {
                            String(personen)
                        },
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
                    .padding(.horizontal, 30)
                    
                    TextField("Sonderwünsche", text: $sonderWünsche)
                        .padding()
                        .background(Color.gray.opacity(0.1))
                        .cornerRadius(10)
                        .padding(.horizontal, 30)
                    
                    Toggle("Kinderstuhl benötigt", isOn: $kinderStuhl)
                        .padding(.horizontal, 30)
                    
                    if selectedTime != nil || formatCustomTime(customTime).isEmpty == false {
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
                                selectedTime: selectedTime ?? formatCustomTime(customTime), // Use formatted custom time
                                peopleCount: personen,
                                specialRequests: sonderWünsche,
                                kinderStuhl: kinderStuhl,
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
        }
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
