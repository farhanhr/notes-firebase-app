# Notes App
This app is just a simple notes app with firebase as authentication and firestore to store notes data

## How to Connect the App to Firebase
1. Go to [Firebase Console](https://console.firebase.google.com).
2. **Create a project** and **register** this app to your firebase project
3. Add all required dependencies and plugin to your gradle files
4. Download ***google-services.json*** and place it in root **app** folder
5. `Sync gradle`

## How to enable the Authentication and Firestore Database
1. Go to Build -> Authentication in your firebase project.
2. Enable Email/Password provider at sign-in method
3. To enable your firestore, go to Build -> Firestore Database.
4. Setup the location and rule


*To see how the app works. Just run the app using emulator or real device*
