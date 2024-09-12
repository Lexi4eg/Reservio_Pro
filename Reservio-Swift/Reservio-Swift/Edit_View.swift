//
//  Edit_View.swift
//  Reservio-Swift
//
//  Created by Felix Prattes on 12.09.24.
//

import SwiftUI
import _PhotosUI_SwiftUI

struct Edit_View: View {
    @ObservedObject var userData: UserData
    @State private var avatarItem: PhotosPickerItem?
    @State private var isImagePickerDisplayed = false

    var body: some View {
        Form {
            Section(header: Text("User Info")) {
                TextField("Firstname", text: $userData.firstname)
                TextField("Lastname", text: $userData.lastname)
                TextField("Email Address", text: $userData.email)
                    .keyboardType(.emailAddress)
                    .autocapitalization(.none)
          
                TextField("Phone Number", text: $userData.telefonNumber)
                    .keyboardType(.phonePad)
                    .autocapitalization(.none)

                Picker("Age", selection: $userData.age) {
                    ForEach(0..<100) { age in
                        Text("\(age)").tag(age)
                    }
                }

                Button(action: {
                    isImagePickerDisplayed = true
                }) {
                    Text("Select Profile Picture")
                }
                .sheet(isPresented: $isImagePickerDisplayed) {
                    ImagePicker(selectedImage: $userData.profilePicture, sourceType: .photoLibrary)
                }

                if let uiImage = UIImage(data: userData.profilePicture) {
                    Image(uiImage: uiImage)
                        .resizable()
                        .scaledToFit()
                        .frame(width: 100, height: 100)
                }
            }

            Section(header: Text("Language")) {
                Picker("Select Language", selection: $userData.selectedLanguage) {
                    ForEach(Language.allCases) { language in
                        Text(language.rawValue.uppercased()).tag(language.rawValue)
                    }
                }
            }
        }
        .navigationBarTitle("Edit Settings", displayMode: .inline)
    }
}

// Image Picker for profile picture
struct ImagePicker: UIViewControllerRepresentable {
    @Binding var selectedImage: Data
    var sourceType: UIImagePickerController.SourceType

    func makeUIViewController(context: Context) -> UIImagePickerController {
        let picker = UIImagePickerController()
        picker.sourceType = sourceType
        picker.delegate = context.coordinator
        return picker
    }

    func updateUIViewController(_ uiViewController: UIImagePickerController, context: Context) {}

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    class Coordinator: NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
        var parent: ImagePicker

        init(_ parent: ImagePicker) {
            self.parent = parent
        }

        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
            if let uiImage = info[.originalImage] as? UIImage,
               let imageData = uiImage.jpegData(compressionQuality: 1) {
                parent.selectedImage = imageData
            }
            picker.dismiss(animated: true)
        }

        func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
            picker.dismiss(animated: true)
        }
    }
}



