# Mood-Meal Mixer 🐿️🍽️

An Android app built with Jetpack Compose that provides personalized meal suggestions based on your mood through psychometry-inspired assessment.

## Overview

Mood-Meal Mixer is a fully offline Android application that:
1. Administers a 5-question mood assessment from a psychometry-inspired pool
2. Suggests 3 simultaneous meal options based on your mood:
   - **Cook at Home** - Simple recipes with nutritional info
   - **Grab from Market** - Ready/semi-ready meal combinations
   - **Order Nearby** - Restaurant suggestions (region-template)
3. After eating (30-90 minutes), conducts a follow-up 5-question test
4. Computes a **Mood Uplift Score (MUS)** = post(valence+arousal) - pre(valence+arousal)
5. Tracks your history with mood uplift metrics

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Navigation**: Navigation Compose
- **Data**: kotlinx-serialization for JSON parsing
- **Animations**: Lottie Compose (with toggleable animations)
- **Min SDK**: 24 (Android 7.0)
- **Target/Compile SDK**: 34 (Android 14)

## Project Structure

```
app/
├── src/main/
│   ├── java/com/moodmeal/mixer/
│   │   ├── data/
│   │   │   ├── Models.kt          # Data classes with serialization
│   │   │   └── Repo.kt            # Repository for loading JSON assets
│   │   ├── domain/
│   │   │   └── Engine.kt          # Mood scoring & suggestion logic
│   │   ├── ui/
│   │   │   ├── AppViewModel.kt    # State management
│   │   │   ├── Screens.kt         # All screen composables
│   │   │   └── SquirrelLottie.kt  # Lottie animation composable
│   │   └── MainActivity.kt        # Main activity with navigation
│   ├── assets/
│   │   ├── questions_en.json      # 10 mood assessment questions
│   │   ├── recipes_en.json        # Recipe database
│   │   ├── market_restaurant_en.json  # Market & restaurant data
│   │   └── squirrel_coach.json    # Lottie animation (placeholder)
│   ├── res/
│   │   └── values/
│   │       └── strings.xml
│   └── AndroidManifest.xml
└── build.gradle
```

## Features

### 1. Onboarding
- Dietary preferences: Vegan, Vegetarian, Halal, Gluten-free
- Animation toggle for accessibility

### 2. Pre-Test (Mood Assessment)
- 5 questions selected from a stratified pool
- Covers 5 mood dimensions: valence, arousal, stress, craving, bodysense
- 5-point Likert scale (1=Disagree to 5=Agree)
- Optional animated "psychologist squirrel" mascot

### 3. Results Screen
- **3 Meal Suggestions** displayed simultaneously:
  - Recipe with time, calories (range), allergens
  - Market combo with quick satiety notes
  - Restaurant option with cuisine and tips
- Portion size slider (0.5x - 1.5x) dynamically adjusts calories
- "Alternative" buttons (stub for future expansion)

### 4. Post-Test
- Another 5-question assessment after eating
- Visual MUS badge:
  - ↑ Green if MUS > +0.1
  - → Gray if -0.1 to +0.1
  - ↓ Red if MUS < -0.1

### 5. History
- Session log with timestamp and MUS
- Shows which meal was selected

## Data Models

### Core Mood Dimensions
```kotlin
enum class MoodDim {
    valence,     // Positive/negative feelings
    arousal,     // Energy level
    stress,      // Tension/pressure
    craving,     // Food desire intensity
    bodysense    // Physical comfort
}
```

### Nutrition Tracking
All meals include detailed nutritional information:
- Calorie ranges (low/est/high)
- Macros: Protein (p), Fat (f), Carbs (c)
- Allergen warnings

### Suggestion Algorithm
Simple rule-based matching:
- **High Stress (≥0.6)**: Warm, soothing meals (soups, complex carbs)
- **High Arousal (≥0.6)**: Fresh, light meals (salads, cold dishes)
- **Default**: Balanced options

## Building & Running

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34

### Build Steps
```bash
# Clone the repository
git clone <repository-url>
cd mood-meal-mixer

# Build with Gradle
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

### Run in Android Studio
1. Open project in Android Studio
2. Wait for Gradle sync
3. Select device/emulator
4. Click Run ▶️

## Performance Targets

✅ Pre-test completion: ≤ 15 seconds  
✅ First render: < 2.5 seconds  
✅ Suggestions screen: < 600ms (local assets only)  
✅ Graceful handling of missing assets (no crashes)

## User Flow

```
Onboarding → Pre-Test (5Q) → Results (3 Cards) → Post-Test (5Q) → MUS Badge → History
     ↓            ↓              ↓                    ↓              ↓          ↓
Set prefs   Answer mood    View/select meal    Answer follow-up   See score  Review
            questions                          questions                      sessions
```

## Future Enhancements

- [ ] Expand question pool to 200 items
- [ ] Add recipe/market/restaurant alternatives
- [ ] Implement dietary preference filtering
- [ ] Cloud sync for cross-device history
- [ ] Chart visualization for MUS trends
- [ ] Regional restaurant integration
- [ ] Social sharing of favorite meals
- [ ] ML-based personalized suggestions

## Data Privacy

✅ **Fully Offline**: No network requests, no tracking  
✅ **Local Storage**: All data stays on device  
✅ **No Permissions**: Doesn't request any Android permissions

## Assets

### JSON Files
- `questions_en.json`: 10 sample mood questions (expandable to 200)
- `recipes_en.json`: 2 recipes (Red Lentil Soup, Tuna Salad)
- `market_restaurant_en.json`: 2 market combos + 1 restaurant option
- `squirrel_coach.json`: Minimal Lottie placeholder (replaceable with full animation)

### Lottie Animation
The app includes a placeholder for the "glasses-wearing psychologist squirrel" animation. 
Replace `app/src/main/assets/squirrel_coach.json` with a full Lottie JSON for enhanced UX.

## Dependencies

```gradle
// Compose BOM
androidx.compose:compose-bom:2024.09.02

// Core
androidx.activity:activity-compose:1.9.2
androidx.compose.material3:material3:1.3.0
androidx.navigation:navigation-compose:2.8.0

// Data
org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3

// Animation
com.airbnb.android:lottie-compose:6.5.0
```

## License

This project is an MVP demonstration app. Intended for educational and portfolio purposes.

## Contributing

This is a solo project built as a proof of concept. Suggestions and feedback are welcome!

---

**Built with ❤️ using Jetpack Compose and Kotlin**
