# Mood-Meal Mixer - Quick Start Guide

## 🚀 Getting Started in 5 Minutes

### Prerequisites
- **Android Studio** Hedgehog (2023.1.1) or later
- **JDK 17** or higher
- **Android SDK 34** installed

### Step 1: Open Project
```bash
# If cloning from repository
git clone <your-repo-url>
cd mood-meal-mixer

# Open in Android Studio
# File → Open → Select this directory
```

### Step 2: Configure SDK Path
Edit `local.properties` and set your Android SDK location:
```properties
sdk.dir=/path/to/your/Android/Sdk
```

**Common SDK locations:**
- macOS: `/Users/[username]/Library/Android/sdk`
- Linux: `/home/[username]/Android/Sdk`
- Windows: `C:\Users\[username]\AppData\Local\Android\Sdk`

### Step 3: Sync Gradle
In Android Studio:
1. Wait for the "Gradle sync" notification
2. Click **"Sync Now"**
3. Wait for dependencies to download (~2-3 minutes first time)

### Step 4: Run the App
1. Connect an Android device (USB debugging enabled) OR start an emulator
2. Click the green **Run ▶️** button in Android Studio
3. Select your device
4. Wait for build and installation

**First launch time:** ~30-60 seconds  
**Subsequent builds:** ~10-15 seconds

---

## 📱 User Flow Demo

### 1️⃣ Onboarding Screen
- Check dietary preferences
- Toggle animations on/off
- Click **Continue**

### 2️⃣ Pre-Test (Mood Assessment)
- Answer 5 questions about your current mood
- Use the 1-5 scale (1=Disagree, 5=Agree)
- See the animated squirrel (if animations enabled)
- Click **Show Suggestions**

### 3️⃣ Results Screen
You'll see **3 meal options**:
- **🍳 Cook at Home**: Quick Red Lentil Soup OR Lemony Tuna Salad
- **🛒 Grab from Market**: Ayran+Simit+Egg OR Kefir+Walnuts+Apple
- **🍽️ Order Nearby**: Chicken Veggie Noodles

Adjust the **portion slider** to see calories update in real-time!

Click **I ate this → Mini Test**

### 4️⃣ Post-Test
- Answer 5 more questions about how you feel now
- Click **Show Score**
- See your **Mood Uplift Score (MUS)**:
  - ↑ Green = Feeling better!
  - → Gray = About the same
  - ↓ Red = Need different food next time
- Click **Save to History**

### 5️⃣ History Screen
- Review all your past meals and MUS scores
- Track patterns over time

---

## 🛠️ Troubleshooting

### Build Errors

**"SDK location not found"**
```bash
# Check local.properties has correct path
cat local.properties
# Should show: sdk.dir=/your/android/sdk/path
```

**"Could not resolve dependencies"**
```bash
# Clear Gradle cache and rebuild
./gradlew clean
./gradlew build --refresh-dependencies
```

**"Unsupported class file version"**
- Your JDK is too old
- Install JDK 17: https://adoptium.net/
- In Android Studio: Settings → Build Tools → Gradle → Gradle JDK → Choose 17

### Runtime Issues

**"App crashes on launch"**
- Check Logcat in Android Studio
- Most common: Missing asset files
  - Verify `app/src/main/assets/` contains all JSON files

**"Squirrel animation not showing"**
- This is expected with the placeholder Lottie file
- You should see 🐿️📝 emoji instead
- Replace `squirrel_coach.json` with real Lottie animation

**"Questions not loading"**
- Verify `questions_en.json` is in `app/src/main/assets/`
- Check JSON syntax with a validator

---

## 🔍 Testing the App

### Manual Testing Checklist
- [ ] Onboarding: Toggle all preferences
- [ ] Pre-Test: Try all answer combinations (1-5)
- [ ] Results: Slide portion to 0.5x and 1.5x
- [ ] Post-Test: Answer differently than pre-test
- [ ] History: Complete 3+ sessions, verify ordering

### Expected MUS Values
- If you answer post-test **higher** than pre-test: **Positive MUS**
- Same answers: **~0.0 MUS**
- Lower answers: **Negative MUS**

MUS formula: `(post_valence + post_arousal) - (pre_valence + pre_arousal)`

---

## 📊 Project Stats

- **Kotlin Code**: ~1,205 lines
- **Screens**: 5 (Onboarding, PreTest, Results, PostTest, History)
- **Data Models**: 14 classes
- **JSON Assets**: 4 files (29 total data items)
- **Dependencies**: 8 core libraries
- **Min SDK**: 24 (covers 95%+ devices)

---

## 🎨 Customization Ideas

### Change Theme Colors
Edit `MainActivity.kt` → `MoodMealMixerTheme`:
```kotlin
MaterialTheme(
    colorScheme = lightColorScheme(
        primary = Color(0xFF6200EA),  // Change this
        secondary = Color(0xFF03DAC5), // And this
        // ... more colors
    )
)
```

### Add More Questions
Edit `app/src/main/assets/questions_en.json`:
```json
{
  "questions": [
    { "id": "q11", "dim": "valence", "text": "Your new question here" }
  ]
}
```

### Add More Recipes
Edit `app/src/main/assets/recipes_en.json`:
```json
{
  "recipes": [
    {
      "id": "r3",
      "name": "Your Recipe Name",
      "timeMin": 20,
      "nutrition": { "kcalLow": 300, "kcalEst": 350, "kcalHigh": 400, "p": 25, "f": 10, "c": 40 },
      ...
    }
  ]
}
```

---

## 🤝 Need Help?

1. Check the main **README.md** for architecture details
2. Review **FEATURES.md** for implementation specifics
3. Examine inline code comments in `.kt` files
4. Common issues: Usually missing assets or SDK path

---

## ✅ Quick Verification

Run this to verify all files are present:
```bash
ls -1 app/src/main/assets/
# Should show:
# market_restaurant_en.json
# questions_en.json
# recipes_en.json
# squirrel_coach.json
```

```bash
ls -1 app/src/main/java/com/moodmeal/mixer/*.kt
# Should show:
# MainActivity.kt
```

```bash
find app/src/main/java -name "*.kt" | wc -l
# Should show: 7 Kotlin files
```

---

**Happy Mood-Meal Mixing! 🐿️🍽️✨**
