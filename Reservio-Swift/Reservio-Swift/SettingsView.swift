//
//  SettingsView.swift
//  SmartMirror
//
//  Created by Felix Prattes on 30.03.24.
//



import SwiftUI
import UIKit
import PhotosUI

public class UserData: ObservableObject {
    @AppStorage("firstname") var firstname: String = ""
    @AppStorage("lastname") var lastname: String = ""
    @AppStorage("age") var age: Int = 0
    @AppStorage("profilePicture") var profilePicture: Data = Data()
    @AppStorage("selectedLanguage") var selectedLanguage = "en"
    @AppStorage("rewardPoints") var rewardPoints: Double = 200.0
    @AppStorage("email") var email: String = ""
    @AppStorage("telefonNumber") var telefonNumber: String = ""


}

enum Language: String, CaseIterable, Identifiable {
    case english = "en"
    case german = "de"

    var id: String {
        self.rawValue
    }
}


struct SettingsView: View {
    @ObservedObject var userData = UserData()
    @State private var avatarItem: PhotosPickerItem?
    @State private var avatarImage: Image?
    @State private var showingEditView = false

    var body: some View {
        VStack(spacing: 20) {
            VStack {
                if let avatarImage = avatarImage {
                    avatarImage
                        .resizable()
                        .scaledToFill()
                        .frame(width: 150, height: 150)
                        .background(.white)
                        .cornerRadius(.infinity)
                } else {
                    PhotosPicker("Select avatar", selection: $avatarItem, matching: .images)
                        .frame(width: 150, height: 150)
                        .background(.white)
                        .cornerRadius(.infinity)
                }
            }
            .onChange(of: avatarItem) {
                Task {
                    if let loaded = try? await avatarItem?.loadTransferable(type: Image.self) {
                        avatarImage = loaded
                    } else {
                        print("Failed")
                    }
                }
            }

            HStack {
                Text(userData.firstname)
                Text(userData.lastname)
            }
            .font(.title2)
            .bold()
            .padding()

            HStack {
                Text("Your Stats")
                    .font(.title)
                    .padding()
            }

            HStack {
                VStack {
                    Text("4").font(.title).bold()
                    Text("Reservations").font(.subheadline).foregroundColor(.gray)
                }
                .frame(maxWidth: .infinity)

                VStack {
                    Text("4").font(.title).bold()
                    Text("Reviews").font(.subheadline).foregroundColor(.gray)
                }
                .frame(maxWidth: .infinity)

                VStack {
                    Text("4").font(.title).bold()
                    Text("Visits").font(.subheadline).foregroundColor(.gray)
                }
                .frame(maxWidth: .infinity)
            }
            .padding(.vertical)
            .background(Color(UIColor.systemBackground))
            .cornerRadius(10)
            .shadow(radius: 4)

            
            
            VStack(spacing: 10) {
                Text("Your Points").font(.title).bold()
                Text("\(Int(userData.rewardPoints)) / 1000").font(.headline).foregroundColor(.gray)

                ZStack(alignment: .leading) {
                    RoundedRectangle(cornerRadius: 10)
                        .fill(Color.gray.opacity(0.3))
                        .frame(height: 20)

                    RoundedRectangle(cornerRadius: 10)
                        .fill(Color.blue)
                        .frame(width: CGFloat(userData.rewardPoints / 1000) * UIScreen.main.bounds.width * 0.8, height: 20)
                }
                .frame(maxWidth: .infinity)
                .padding()
            }
            .padding(.vertical)
            .background(Color(UIColor.systemBackground))
            .cornerRadius(15)
            .shadow(radius: 5)
            .frame(maxWidth: 350)

            Button("Edit Profile") {
                showingEditView = true
            }
            .sheet(isPresented: $showingEditView) {
                Edit_View(userData: userData)
            }

            Spacer()
        }
        .navigationBarTitle("Settings")
        .padding()
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
    }
}
