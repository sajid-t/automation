# PW Thor Mini Browser (starter source)

Android WebView mini-browser starter. Includes URL entry, Go, Back, Reload, Home, loading progress, and navigation status/logging.

## Build from an Android phone
1. Download and unzip this project.
2. Open GitHub in your phone browser and create a repository.
3. Upload the project files/folders preserving their paths.
4. Add a GitHub Actions workflow that builds the Android debug APK, or use an Android-compatible cloud IDE/build service.
5. Download the generated APK artifact and install it after reviewing Android's install warning.

## Important
- This starter tracks top-level WebView navigations; it does not guarantee every internal redirect is exposed.
- It does not inject third-party JavaScript or bypass ad timers, CAPTCHA, access checks, or token/device restrictions.
- Do not log or share URLs containing access tokens. The starter logs navigated URLs to Android Logcat; remove query strings from logging before distributing widely.
- For production, update dependencies/toolchain, review privacy/security, and test only sites you are authorized to access.
