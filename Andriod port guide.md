# Porting your iOS app to **Android** with *Cursor AI*

> **Goal:** replicate the “Vibe” coding style (concise, reactive, Compose‑first) using nothing but the command‑line toolchain and Cursor (VS Code clone).

---

## 1️⃣  Local toolchain (Ubuntu 24.04 example)

| Purpose                                                                                                                                                                             | Tool / Version                                                      | One‑liner                         |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------- | --------------------------------- |
| Java runtime & compiler                                                                                                                                                             | **OpenJDK 21**                                                      | `sudo apt install openjdk-21-jdk` |
| Android SDK & CLI                                                                                                                                                                   | *commandline‑tools* r11076708 → `/opt/android/cmdline-tools/latest` | \`\`\`bash                        |
| sudo mkdir -p /opt/android && cd /opt/android                                                                                                                                       |                                                                     |                                   |
| curl -O [https://dl.google.com/android/repository/commandlinetools-linux-11076708\_latest.zip](https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip) |                                                                     |                                   |
| sudo unzip commandlinetools-linux-\*.zip -d cmdline-tools                                                                                                                           |                                                                     |                                   |
| sudo mv cmdline-tools/cmdline-tools cmdline-tools/latest                                                                                                                            |                                                                     |                                   |
| sudo chown -R \$USER:\$USER /opt/android                                                                                                                                            |                                                                     |                                   |

````|
| Required SDK packages | platform‑tools, build‑tools 35.0.0, platform 35, emulator, system‑image | ```bash
export ANDROID_HOME=/opt/android
export PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH
sdkmanager "platform-tools" \
           "platforms;android-35" \
           "build-tools;35.0.0" \
           "emulator" \
           "system-images;android-35;google_apis;x86_64"
``` |
| Hardware accel | **KVM** + libvirt | `sudo apt install qemu-kvm libvirt-daemon-system libvirt-clients bridge-utils && sudo adduser $USER kvm && newgrp kvm` |
| Emulator (AVD) | Pixel 6, Android 35 | ```bash
yes | avdmanager create avd -n pixel35 \
      -k "system-images;android-35;google_apis;x86_64" \
      --device "pixel_6"
emulator -avd pixel35 -netdelay none -netspeed full -qemu -enable-kvm
``` |
| Build system | **Gradle 8.5 wrapper** (included below) | none – project ships its own `gradlew` |

Add the two `export` lines to `~/.bashrc` / `~/.zshrc` so `sdkmanager`, `avdmanager`, & `emulator` stay on the PATH.

---
## 2️⃣  Project skeleton (Compose‑first)
````

MyHobbyApp/                      # Git root
├─ settings.gradle.kts
├─ build.gradle.kts             # single‑module build
├─ gradle/                      # wrapper JARs
├─ gradlew\*                     # wrapper scripts
└─ app/
├─ src/main/java/com/example/myhobby/MainActivity.kt
├─ src/main/AndroidManifest.xml
└─ src/main/res/             # icons, values, etc.

````

### `settings.gradle.kts`
```kotlin
pluginManagement { repositories { google(); gradlePluginPortal() } }
rootProject.name = "MyHobbyApp"
include(":app")
````

### Root `build.gradle.kts`

```kotlin
plugins {
    id("com.android.application") version "8.5.0" apply false
    kotlin("android") version "2.0.0" apply false
}
```

### `app/build.gradle.kts`

```kotlin
plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.myhobby"
    compileSdk = 35
    defaultConfig {
        applicationId = namespace
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.debug // good enough for friends
        }
    }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "2.0.0" }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.0")
}
```

### Minimal `MainActivity.kt`

```kotlin
package com.example.myhobby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = dynamicLightColorScheme(this)) {
                var clicks by remember { mutableIntStateOf(0) }
                Button(onClick = { clicks++ }) {
                    Text("Hallo Android – $clicks")
                }
            }
        }
    }
}
```

> **Vibe style cues:**  single‑screen, reactive `remember` state, minimal boilerplate, Material 3.

---

## 3️⃣  Build & deploy commands

| Action                                                | CLI (run from project root)                                                   |
| ----------------------------------------------------- | ----------------------------------------------------------------------------- |
| Assemble + install on first connected device/emulator | `./gradlew installDebug`                                                      |
| Assemble release APK                                  | `./gradlew assembleRelease` → `app/build/outputs/apk/release/app-release.apk` |
| Open logcat stream                                    | `adb logcat -T 1`                                                             |
| Hot‑reload (live edit)                                | just **save** in Cursor → Gradle triggers Compose Live Edit                   |

---

## 4️⃣  Distribute to friends

### A) Quick share

```
cp app/build/outputs/apk/release/app-release.apk ~/Dropbox/Links
# send URL / QR code
```

Friends must enable *Install Unknown Apps*.

### B) Private F‑Droid repo (auto‑updates)

```bash
pip install fdroidserver
mkdir myrepo && cd myrepo
fdroid init
cp ../app-release.apk repo/
fdroid update --create-metadata
python -m http.server 8080   # quick test
# later: push repo/ to GitHub Pages or Netlify
```

Add the repository URL + fingerprint in the F‑Droid client once – updates flow automatically.

---

## 5️⃣  Bring your iOS logic over

* **Shared business layer** → convert Swift → Kotlin *or* extract to Kotlin Multiplatform Core module.
* Replace Combine/SwiftUI bindings with `StateFlow`/`remember { mutableStateOf() }`.
* Common networking: Ktor 2.x or Retrofit 2/3.
* CoreData → `Room` (SQL) or `DataStore` for KV pairs.

---

## 6️⃣  Handy CLI cheatsheet

```bash
adb devices                  # list connected targets
adb -s emulator-5554 emu kill   # stop specific AVD
adb install -r app-release.apk  # manual sideload (‑r = replace)

gradle wrapper --gradle-version 8.5   # upgrade wrapper
sdkmanager --list                    # show installed/remote packages
```

---

## 7️⃣  Folder recap

```
/opt/android/                  # SDK root
  ├─ cmdline-tools/latest/
  ├─ platform-tools/adb
  └─ platforms/android-35/
~/Android/Sdk/avd/pixel35.ini  # AVD config (default location)
<project>/                     # your app (Git repo)
fdroid-repo/                   # optional private store (index.xml, .apk)
```

---

## 8️⃣  Troubleshooting

| Symptom                          | Quick fix                                                           |
| -------------------------------- | ------------------------------------------------------------------- |
| `sdkmanager: command not found`  | `echo $PATH` – check `cmdline-tools/latest/bin`                     |
| AVD starts painfully slow        | verify `-enable-kvm` & run `kvm-ok` (package `cpu-checker`)         |
| APK not installing on Android 15 | `targetSdk` must be **≥ 30** & `minSdk` ≥ 24                        |
| Gradle build fails on first sync | `./gradlew --stop && ./gradlew clean` then reopen project in Cursor |

---

### 🎉 You’re ready

Open the folder in **Cursor AI**, tell the AI *“build me screen X in Jetpack Compose using vibe style”*, and iterate. The CLI toolchain handles builds/emulators; Cursor handles codegen & refactorings.

Happy porting! 🚀
