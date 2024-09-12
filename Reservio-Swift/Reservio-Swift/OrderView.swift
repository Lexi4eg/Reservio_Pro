//
//  OrderView.swift
//  Reservio-Swift
//
//  Created by Felix Prattes on 12.09.24.
//

import SwiftUI

struct OrderView: View {
    
    let order = Order(
            id: "5d4f0e49-9fe1-4d1c-a6e7-9b0fcb2094d5",
            firstName: "John",
            lastName: "Doe",
            date: "2024-09-12T08:23:51.702+0000",
            peopleCount: 4,
            email: "john.doe@example.com",
            phone: "123-456-7890",
            specialRequests: "Ich mag etwas",
            highChair: 1
        )
    
    var body: some View {
        VStack(spacing: 20) {
            VStack {
                           Text(order.firstName)
                               .font(.system(size: 34, weight: .bold, design: .default))
                               .foregroundColor(.primary)
                           Text(order.lastName)
                               .font(.system(size: 34, weight: .light, design: .default))
                               .foregroundColor(.secondary)
                       }
                       .padding(.top, 40)
                   
                   List {
                      
                       HStack {
                                          Text("Date")
                                          Spacer()
                                          Text(formatDate(order.date))
                                              .foregroundColor(.gray)
                                      }
                       
                      
                       HStack {
                           Text("People")
                           Spacer()
                           Text("\(order.peopleCount) people")
                               .foregroundColor(.gray)
                       }
                       
                      
                       HStack {
                           Text("Email")
                           Spacer()
                           Text(order.email)
                               .foregroundColor(.gray)
                       }
                       
                      
                       HStack {
                           Text("Phone")
                           Spacer()
                           Text(order.phone)
                               .foregroundColor(.gray)
                       }
                       
                       
                       HStack {
                           Text("Special Requests")
                           Spacer()
                           Text(order.specialRequests)
                               .foregroundColor(.gray)
                       }
                       
                      
                       HStack {
                           Text("High Chair")
                           Spacer()
                           Text(order.highChair > 0 ? "\(order.highChair) requested" : "None")
                               .foregroundColor(.gray)
                       }
                   }
                   .listStyle(InsetGroupedListStyle())
            
                   Button(action: {
                       print("Order confirmed")
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
               .padding()
               .navigationBarTitle("Order Summary", displayMode: .inline)
           }
           
    func formatDate(_ dateStr: String) -> String {
        let isoFormatter = ISO8601DateFormatter()
        if let date = isoFormatter.date(from: dateStr) {
            let displayFormatter = DateFormatter()
            displayFormatter.dateStyle = .short
            displayFormatter.timeStyle = .short
            return displayFormatter.string(from: date)
        }
        return dateStr
    }

}
      

struct Order {
    let id: String
    let firstName: String
    let lastName: String
    let date: String
    let peopleCount: Int
    let email: String
    let phone: String
    let specialRequests: String
    let highChair: Int
}


#Preview {
    OrderView()
}
