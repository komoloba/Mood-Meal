# Build Instructions - Mood-Meal Mixer

## 🎯 Quick Build (3 Steps)

### Step 1: Configure SDK Path
```bash
# Edit local.properties and set your Android SDK path
# Example paths:
# macOS:   sdk.dir=/Users/yourname/Library/Android/sdk
# Linux:   sdk.dir=/home/yourname/Android/Sdk
# Windows: sdk.dir=C:\\Users\\yourname\\AppData\\Local\\Android\\Sdk
```

### Step 2: Build the APK
```bash
# From project root directory
./gradlew assembleDebug
```

### Step 3: Install on Device
```bash
# Connect Android device via USB (with USB debugging enabled)
# OR start an Android emulator
./gradlew installDebug
```

**Done!** The app will launch automatically.

---

## 🖥️ Using Android Studio (Recommended)

### 1. Open Project
1. Launch Android Studio
2. Click "Open" or "File → Open"
3. Navigate to the project directory
4. Select the root folder (containing `build.gradle`)
5. Click "OK"

### 2. Wait for Gradle Sync
- Android Studio will automatically sync Gradle
- First-time sync downloads dependencies (~100-200 MB)
- Progress shown in bottom status bar
- Wait for "Gradle sync finished" message

### 3. Configure Run Configuration (Usually Automatic)
1. Top toolbar: Select "app" in the dropdown
2. Select target device (connected device or emulator)
3. Click green Run button ▶️

### 4. Build & Run
- **Build**: Build → Make Project (Ctrl+F9 / Cmd+F9)
- **Run**: Run → Run 'app' (Shift+F10 / Ctrl+R)
- **Debug**: Run → Debug 'app' (Shift+F9 / Ctrl+D)

---

## 🔧 Command Line Build Options

### Debug Build (Development)
```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Release Build (For Distribution)
```bash
# Build release APK (unsigned)
./gradlew assembleRelease

# Note: Requires signing configuration in build.gradle
# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

### Install Commands
```bash
# Install debug build
./gradlew installDebug

# Uninstall app
./gradlew uninstallDebug

# Install and immediately launch
./gradlew installDebug && adb shell am start -n com.moodmeal.mixer/.MainActivity
```

### Useful Build Commands
```bash
# Clean all build artifacts
./gradlew clean

# Run code quality checks
./gradlew check

# List all available tasks
./gradlew tasks

# Build with stacktrace for debugging
./gradlew assembleDebug --stacktrace

# Refresh dependencies
./gradlew build --refresh-dependencies
```

---

## 📱 Device Setup

### Physical Device
1. **Enable Developer Options**:
   - Settings → About Phone
   - Tap "Build Number" 7 times
   - "You are now a developer!" message appears

2. **Enable USB Debugging**:
   - Settings → System → Developer Options
   - Toggle "USB Debugging" ON
   - Connect device via USB
   - Authorize computer when prompted

3. **Verify Connection**:
   ```bash
   adb devices
   # Should show: List of devices attached
   #              XXXXXXXXXXXXXX    device
   ```

### Emulator (Android Studio)
1. **Create Emulator**:
   - Tools → Device Manager
   - Click "Create Device"
   - Select "Pixel 5" or similar
   - Choose System Image: API 34 (Android 14) or API 24+ (minimum)
   - Click "Finish"

2. **Start Emulator**:
   - Device Manager → Click ▶️ next to emulator
   - Wait for boot (~30 seconds)

3. **Verify**:
   ```bash
   adb devices
   # Should show emulator-5554 or similar
   ```

---

## 🐛 Troubleshooting

### Issue: "SDK location not found"
**Solution**:
```bash
# Create/edit local.properties
echo "sdk.dir=/path/to/your/Android/Sdk" > local.properties

# Find your SDK path:
# macOS/Linux: ls ~/Library/Android/sdk  or  ls ~/Android/Sdk
# Windows: dir %LOCALAPPDATA%\Android\Sdk
```

### Issue: "Could not resolve dependencies"
**Solution**:
```bash
# Check internet connection
# Clear Gradle cache
./gradlew clean
rm -rf ~/.gradle/caches/

# Re-sync
./gradlew build --refresh-dependencies
```

### Issue: "Unsupported class file version"
**Solution**:
```bash
# Check JDK version
java -version
# Should be 17 or higher

# In Android Studio:
# Settings → Build, Execution, Deployment → Build Tools → Gradle
# Set Gradle JDK to "17" or "Embedded JDK (17)"
```

### Issue: "AAPT out of memory"
**Solution**:
```properties
# Add to gradle.properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

### Issue: "Execution failed for task ':app:processDebugResources'"
**Solution**:
```bash
# Clean and rebuild
./gradlew clean
./gradlew assembleDebug --info
```

### Issue: "No connected devices"
**Solution**:
```bash
# List devices
adb devices

# If none listed:
# - Check USB cable
# - Re-enable USB debugging
# - Try different USB port
# - Restart adb: adb kill-server && adb start-server
```

### Issue: "App crashes on startup"
**Solution**:
```bash
# View logs
adb logcat | grep "AndroidRuntime"

# Or in Android Studio:
# View → Tool Windows → Logcat
# Filter by "com.moodmeal.mixer"
```

---

## 📦 Build Outputs

### Debug APK Location
```
app/build/outputs/apk/debug/app-debug.apk
```

### Release APK Location
```
app/build/outputs/apk/release/app-release-unsigned.apk
```

### Install Debug APK Manually
```bash
# Via ADB
adb install app/build/outputs/apk/debug/app-debug.apk

# Or copy to device and tap to install
# (Requires "Install from Unknown Sources" enabled)
```

---

## 🧪 Testing the Build

### 1. Verify App Launches
```bash
# Launch app
adb shell am start -n com.moodmeal.mixer/.MainActivity

# Should see Onboarding screen
```

### 2. Check Assets Loaded
```bash
# Monitor logs while navigating to Pre-Test screen
adb logcat | grep "MoodMeal"

# Should NOT see JSON parsing errors
```

### 3. Complete Full Flow
1. ✅ Onboarding → Set preferences → Continue
2. ✅ Pre-Test → Answer 5 questions → Show Suggestions
3. ✅ Results → View 3 cards → Slide portion → Mini Test
4. ✅ Post-Test → Answer 5 questions → Show Score → MUS badge
5. ✅ History → See saved session

### 4. Test Edge Cases
- All questions answered with "1" (minimum)
- All questions answered with "5" (maximum)
- Disable animations → No squirrel, should show placeholder
- Multiple sessions → History should show all

---

## 🚀 Performance Verification

### Measure Build Time
```bash
time ./gradlew clean assembleDebug
# Should complete in < 2 minutes (first build may take longer)
```

### Measure App Startup
```bash
# Clear app data
adb shell pm clear com.moodmeal.mixer

# Launch and measure
adb shell am start -W com.moodmeal.mixer/.MainActivity
# Check "TotalTime" value
# Should be < 2500ms for first launch
```

### Check APK Size
```bash
ls -lh app/build/outputs/apk/debug/app-debug.apk
# Should be ~5-10 MB
```

---

## 📊 Build Variants

### Debug (Default)
- Includes debugging symbols
- Not obfuscated
- Larger APK size
- Use for development

```bash
./gradlew assembleDebug
```

### Release
- Optimized code
- Smaller APK size
- Requires signing for distribution

```bash
./gradlew assembleRelease
# Note: Configure signing in app/build.gradle first
```

---

## 🔐 Signing (For Release Builds)

### Generate Keystore (One Time)
```bash
keytool -genkey -v -keystore release-key.keystore \
  -alias moodmeal -keyalg RSA -keysize 2048 -validity 10000

# Follow prompts to set password and details
```

### Configure Signing in build.gradle
```gradle
android {
    signingConfigs {
        release {
            storeFile file("release-key.keystore")
            storePassword "your-password"
            keyAlias "moodmeal"
            keyPassword "your-password"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### Build Signed Release
```bash
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk
```

---

## 🎓 Gradle Tips

### Speed Up Builds
```properties
# Add to gradle.properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
android.enableBuildCache=true
```

### Offline Builds
```bash
./gradlew assembleDebug --offline
# Uses cached dependencies
```

### Force Re-download Dependencies
```bash
./gradlew clean build --refresh-dependencies
```

---

## ✅ Verification Checklist

Before considering build successful:

- [ ] `./gradlew assembleDebug` completes without errors
- [ ] APK file exists in `app/build/outputs/apk/debug/`
- [ ] `./gradlew installDebug` installs successfully
- [ ] App launches on device/emulator
- [ ] Onboarding screen displays correctly
- [ ] Assets load (questions, recipes visible)
- [ ] Navigation works (all 5 screens accessible)
- [ ] No crashes in Logcat
- [ ] Lottie placeholder shows (🐿️📝 emoji if animation missing)

---

## 📞 Need Help?

### Check Logs
```bash
# Real-time logs
adb logcat | grep "AndroidRuntime"

# Save logs to file
adb logcat > logcat.txt
```

### Common Log Filters
```bash
# Errors only
adb logcat *:E

# App-specific
adb logcat | grep "com.moodmeal.mixer"

# Clear logs
adb logcat -c
```

### Android Studio Logs
1. View → Tool Windows → Logcat
2. Select device
3. Filter: "com.moodmeal.mixer"
4. Look for red error messages

---

**Build time**: ~1-2 minutes (clean build)  
**Install time**: ~10-15 seconds  
**First launch**: ~2-3 seconds  

**Ready to build and enjoy! 🐿️🍽️✨**
