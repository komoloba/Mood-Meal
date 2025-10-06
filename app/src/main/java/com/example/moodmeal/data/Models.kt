package com.example.moodmeal.data

import kotlinx.serialization.Serializable

@Serializable
enum class MoodDim { valence, arousal, stress, craving, bodysense }

@Serializable
data class MoodQuestion(
    val id: String,
    val dim: MoodDim,
    val text: String,
)

@Serializable
data class Nutrition(
    val kcalLow: Int,
    val kcalEst: Int,
    val kcalHigh: Int,
    val p: Int,
    val f: Int,
    val c: Int,
)

@Serializable
data class Tags(
    val moods: List<String> = emptyList(),
    val diet: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),
    val cuisine: List<String> = emptyList(),
    val season: List<String> = emptyList(),
)

@Serializable
data class Ingredient(
    val name: String,
    val g: Int,
)

@Serializable
data class Recipe(
    val id: String,
    val name: String,
    val timeMin: Int,
    val nutrition: Nutrition,
    val tags: Tags,
    val allergens: List<String> = emptyList(),
    val ingredients: List<Ingredient> = emptyList(),
    val steps: List<String> = emptyList(),
    val image: String? = null,
)

@Serializable
data class MarketItem(
    val id: String,
    val name: String,
    val kcalPerPortion: Int,
    val allergens: List<String> = emptyList(),
    val notes: String = "",
)

@Serializable
data class RestaurantItem(
    val id: String,
    val name: String,
    val cuisine: String,
    val nutrition: Nutrition,
    val allergens: List<String> = emptyList(),
    val tips: String = "",
)

// Non-serializable app models

data class Suggestion(
    val recipe: Recipe?,
    val market: MarketItem?,
    val restaurant: RestaurantItem?,
)

data class Scores(
    var valence: Double = 0.5,
    var arousal: Double = 0.5,
    var stress: Double = 0.5,
    var craving: Double = 0.5,
    var bodysense: Double = 0.5,
)

data class Session(
    val preAnswers: List<Int> = emptyList(),
    val preScores: Scores = Scores(),
    val suggestion: Suggestion = Suggestion(null, null, null),
    val postAnswers: List<Int> = emptyList(),
    val postScores: Scores = Scores(),
    val mus: Double = 0.0,
    val ts: Long = System.currentTimeMillis(),
)
