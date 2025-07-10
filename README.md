This is a Kotlin Multiplatform project targeting Android, iOS.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…


<p align="center">
  <img src="https://github.com/user-attachments/assets/62966df0-7bee-4399-9798-1b40eb398b08" width="45%" alt="Screenshot 1" />
  <img src="https://github.com/user-attachments/assets/adeb5822-7fa8-40b0-9e53-a78db62c69c1" width="45%" alt="Screenshot 2" />
</p>
