import SwiftUI

struct OrderView: View {
    let date: Date
    let selectedTime: String
    let peopleCount: Int
    let specialRequests: String
    let kinderStuhl: Bool

    @ObservedObject var userData: UserData
    @Environment(\.presentationMode) var presentationMode
    @State private var isProcessing = false

    var body: some View {
        VStack(spacing: 20) {
            if isProcessing {
                loadingView
                    .transition(.opacity)
            } else {
                VStack {
                    Text(userData.firstname)
                        .font(.system(size: 34, weight: .bold, design: .default))
                        .foregroundColor(.primary)
                    Text(userData.lastname)
                        .font(.system(size: 34, weight: .light, design: .default))
                        .foregroundColor(.secondary)
                }
                .padding(.top, 40)
                
                List {
                    HStack {
                        Text("Date & Time")
                        Spacer()
                        Text(formatDate(date, time: selectedTime))
                            .foregroundColor(.gray)
                    }
                    
                    HStack {
                        Text("People")
                        Spacer()
                        Text("\(peopleCount) people")
                            .foregroundColor(.gray)
                    }
                    
                    HStack {
                        Text("Email")
                        Spacer()
                        Text(userData.email)
                            .foregroundColor(.gray)
                    }
                    
                    HStack {
                        Text("Phone")
                        Spacer()
                        Text(userData.telefonNumber)
                            .foregroundColor(.gray)
                    }
                    
                    HStack {
                        Text("Special Requests")
                        Spacer()
                        Text(specialRequests)
                            .foregroundColor(.gray)
                    }
                    
                    HStack {
                        Text("High Chair")
                        Spacer()
                        Text(kinderStuhl ? "Requested" : "None")
                            .foregroundColor(.gray)
                    }
                }
                .listStyle(InsetGroupedListStyle())
                
                Button(action: {
                    Task {
                        startPaymentProcess()
                        let res1 =  Reservation(id: "test", firstName: "Felix", lastName: "Prattes", date: "Test", peopleCount: 4,  email: "felix@prattes.com", phoneNumber: "+49123456789", specialRequests: "", highChair: true)
                        userData.rewardPoints += 100;
                        userData.reservationCount += 1;
                        
                        
                        await sendReservationRequest(requestBody: res1)
                        
                    }
                    
                }) {
                    Text("Confirm Order")
                        .font(.headline)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
                .padding(.horizontal, 20)
            }
        }
        .padding()
        .navigationBarTitle("Order Summary", displayMode: .inline)
    }
    
    private func formatDate(_ date: Date, time: String) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .short
        dateFormatter.timeStyle = .none
        let dateString = dateFormatter.string(from: date)
        
        return "\(dateString) \(time)"
    }

    private var loadingView: some View {
        VStack {
            Circle()
                .stroke(Color.blue, lineWidth: 2)
                .frame(width: 100, height: 100)
                .overlay(
                    Image(systemName: "checkmark")
                        .font(.system(size: 40))
                        .foregroundColor(.blue)
                )
                .scaleEffect(isProcessing ? 1.2 : 1.0)
                .animation(Animation.easeInOut(duration: 0.5).repeatForever(autoreverses: true), value: isProcessing)
            
            Text("Processing...")
                .font(.headline)
                .padding()
                .foregroundColor(.blue)
        }
    }

    private func startPaymentProcess() {
        isProcessing = true
        
        // Simulate payment processing
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) { // Adjust delay as needed
            isProcessing = false
            // Pop to root view after processing
            presentationMode.wrappedValue.dismiss()
        }
    }
}

struct OrderView_Previews: PreviewProvider {
    static var previews: some View {
        OrderView(
            date: Date(),
            selectedTime: "19:00",
            peopleCount: 4,
            specialRequests: "Test request",
            kinderStuhl: true,
            userData: UserData()
        )
    }
}
