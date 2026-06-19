[![Stand With Ukraine](https://raw.githubusercontent.com/vshymanskyy/StandWithUkraine/main/banner-direct-single.svg)](https://stand-with-ukraine.pp.ua)
[![Upload Android Build to App Tester](https://github.com/Turskyi/MalaKnyzhka/actions/workflows/android_compose_ci.yml/badge.svg?event=push)](https://github.com/Turskyi/MalaKnyzhka/actions/workflows/android_compose_ci.yml)
[![Deploy to Firebase Hosting on PR](https://github.com/Turskyi/MalaKnyzhka/actions/workflows/firebase-hosting-pull-request.yml/badge.svg)](https://github.com/Turskyi/MalaKnyzhka/actions/workflows/firebase-hosting-pull-request.yml)
[![Deploy to Firebase Hosting on merge](https://github.com/Turskyi/MalaKnyzhka/actions/workflows/firebase-hosting-merge.yml/badge.svg)](https://github.com/Turskyi/MalaKnyzhka/actions/workflows/firebase-hosting-merge.yml)
[![Codemagic build status](https://api.codemagic.io/apps/678d89827cdba5cbbb772f7a/android-compose-multiplatform-workflow/status_badge.svg)](https://codemagic.io/apps/678d89827cdba5cbbb772f7a/android-compose-multiplatform-workflow/latest_build)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/Turskyi/MalaKnyzhka)
[![wakatime](https://wakatime.com/badge/user/f9df5074-b4ea-4c17-b001-fff428ab82aa/project/87bd0f70-41ef-4cf6-86db-4239d2a23599.svg)](https://wakatime.com/badge/user/f9df5074-b4ea-4c17-b001-fff428ab82aa/project/87bd0f70-41ef-4cf6-86db-4239d2a23599)
<img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/m/Turskyi/MalaKnyzhka">

# Мала Книжка ✦ Тарас Шевченко

Welcome to "Мала Книжка ✦ Тарас Шевченко", a Compose Multiplatform project
designed to deliver a cohesive reading experience across Android, iOS, Web, and
Desktop. This project aims to celebrate the lyrical beauty of Taras Shevchenko's
poetry.

### Testing the App:

Join our testing program and provide valuable feedback:

- [Android App Tester Invite](https://appdistribution.firebase.dev/i/598cf84a44dfa4de)
- [iOS TestFlight Invite](https://testflight.apple.com/join/cEN4y79T)

## PROJECT SPECIFICATION

• Programming language: [Kotlin](https://kotlinlang.org/);

• Framework:
[Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/);

• SDK: [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html);

• Interface: [Compose](https://developer.android.com/jetpack/compose);

• Version control system: [Git](https://git-scm.com);

• Git Hosting Service: [GitHub](https://github.com);

• **Code Readability:** code is easily readable with no unnecessary blank
lines, no unused variables or methods, and no commented-out code, all
variables, methods, and resource IDs are descriptively named such that another
developer reading the code can easily understand their function.

• Architectural pattern:
[Monolith](https://learn.microsoft.com/en-us/dotnet/architecture/modern-web-apps-azure/common-web-application-architectures#all-in-one-applications);

## Project Structure

- **`/composeApp`**: Contains shared code for the Compose Multiplatform
  applications.

  - **`commonMain`**: Houses the common code for all targets.
  - **Platform-specific folders** (e.g., `iosMain`, `androidMain`): These
    contain code for specific platforms like CoreCrypto for iOS in the
    `iosMain` folder.

- **`/iosApp`**: Contains the iOS applications. Despite sharing UI code, this
  serves as the entry point for the iOS app and can include Swift/SwiftUI code
  if needed.

## About the Project

### Overview

"Мала Книжка ✦ Тарас Шевченко" is a digital collection of Taras Shevchenko's
works, offering a seamless reading experience with draggable dividers and
elegant page transitions. It supports multiple platforms through Compose
Multiplatform, sharing a unified codebase.

### Features

- **Platform Support**: A single codebase approach for Android, iOS, Web, and
  Desktop.
- **Interactive UI**: Draggable dividers allowing users to switch between the
  text and images effortlessly.
- **Swipe Gestures**: Smooth page transitions with horizontal drag gestures.
- **Multilingual Support**: Access Shevchenko's poetry in its native language.
- **Authentic Scans**: Includes scanned book spreads of the original "Мала
  Книжка" with the actual handwriting of Taras Shevchenko, offering a glimpse
  into the early Ukrainian language and script.

## Credits

This project utilizes book spreads and texts from the website
[t-shevchenko.name](https://www.t-shevchenko.name/uk/Gallery/Works/1850MalaKn.html).
The site's creators kindly allow reproduction with proper referencing. \*
\*Передрук статей із сайту заохочується за умови посилання (гіперпосилання) на
наш сайт\*\*. Many thanks for their valuable contributions.

The implementation of this project was inspired by the
[Get started with Compose Multiplatform – tutorial](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-getting-started.html)
provided by [JetBrains](https://github.com/JetBrains). Their resources were
instrumental in guiding the development process.

## Getting Started

To get started, ensure you have the necessary development environments for each
target platform. For detailed guidelines, refer to the following:

- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform)
- [Kotlin/Wasm](https://kotl.in/wasm/)

### Building and Running the Project

#### Web Application
Build and run the web application using this Gradle task:

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

**Browser Compatibility:** The web application is specifically designed to run
within Google Chrome. While other browsers may technically load the
application, they may not display it correctly or support all features. For
the best experience, use Google Chrome.

#### Backend AI Server

To run the AI backend server locally:

1. **Configure Environment Variables**:
   Create a `server/.env` file in the project root and add your API keys:
   ```env
   GROQ_API_KEY=your_key
   MISTRAL_API_KEY=your_key
   GEMINI_API_KEY=your_key
   ```

2. **Run the Server**:
   ```bash
   ./gradlew :server:run
   ```

3. **Run Tests**:
   To run the backend unit tests (fallback logic, API structure, etc.):
   ```bash
   ./gradlew :server:test
   ```

4. **Stop the Server**:
    - In the terminal: Press `Control + C`.
    - In Android Studio: Click the **Red Square (Stop)** button in the Run tool
      window.
    - If the process is stuck: `kill -9 $(lsof -t -i:8080)` (assuming default
      port 8080).

4. **Test the API**:
   You can test the chat endpoint using `curl`:
   ```bash
   curl -X POST http://localhost:8080/chat \
   -H "Content-Type: application/json" \
   -d '{
     "message": "Привіт, Тарасе! Яку пораду ти даси молоді?",
     "pageNumber": 10,
     "pageText": "Учітесь, читайте, і чужому научайтесь, й свого не цурайтесь."
   }'
   ```

   **Example Response**:
   ```json
   {
       "answer": "Добрий день, молодій людино! Як ти бачиш на сторінці 10 моєї \"Малої Книжки\"...",
       "providerUsed": "groq"
   }
   ```

## How to Contribute

We welcome contributions to enhance this project. Here&#39;s how you can
contribute:

- **Report Issues**: Encountered a bug or have a suggestion? Report issues
  on [GitHub](https://github.com/JetBrains/compose-multiplatform/issues).

Enjoy the poetic journey! 🌟

## Screenshots:

<!--suppress CheckImageSize -->
<img src="screenshots/Screenshot_book_and_text.png" width="400"  alt="screenshot">
<!--suppress CheckImageSize -->
<img src="screenshots/Screenshot_book_spread.png" width="400"  alt="screenshot">
<!--suppress CheckImageSize -->
<img src="screenshots/Screenshot_text.png" width="400"  alt="screenshot">
<!--suppress CheckImageSize -->
<img src="screenshots/Screenshot_landscape_tablet.png" width="400"  alt="screenshot">

## Download

<a href="https://play.google.com/store/apps/details?id=com.turskyi.malaknyzhka" target="_blank">
<img src="https://play.google.com/intl/en_gb/badges/static/images/badges/en_badge_web_generic.png" width=240  alt="google play badge"/>
</a>
