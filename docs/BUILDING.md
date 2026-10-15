# Building NeXtep

## Toolchain

- Android Studio with Android SDK 36.1 installed
- JDK 21 for Gradle and Android Gradle Plugin execution
- Gradle Wrapper 9.5.0, included in this repository
- Android Gradle Plugin 9.3.0

The application is written in Kotlin with JVM 17 bytecode compatibility. The project targets API 36, has a minimum API of 35, and resolves dependencies from Google Maven and Maven Central.

Create `local.properties` locally if Android Studio does not generate it:

```properties
sdk.dir=C\:\\path\\to\\Android\\Sdk
```

## Build commands

On Windows PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug
```

On macOS or Linux:

```bash
./gradlew :app:assembleDebug
```

The debug APK is written below `app/build/outputs/apk/debug/`.

Configure release signing locally before producing a distributable APK.

## Device validation

Because NeXtep hooks privileged system processes, validate changes on a recoverable test device and confirm that LSPosed can be disabled during recovery.
