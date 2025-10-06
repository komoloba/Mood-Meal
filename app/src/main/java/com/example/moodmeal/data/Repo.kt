package com.example.moodmeal.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

@Serializable
private data class QuestionsRoot(val questions: List<MoodQuestion>)

@Serializable
private data class RecipesRoot(val recipes: List<Recipe>)

@Serializable
private data class MarketRestaurantRoot(
    val marketItems: List<MarketItem>,
    val restaurantItems: List<RestaurantItem>,
)

class Repo(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    private fun readAsset(name: String): String {
        return context.assets.open(name).bufferedReader().use { it.readText() }
    }

    fun loadQuestions(): List<MoodQuestion> {
        return try {
            val txt = readAsset("questions_en.json")
            json.decodeFromString<QuestionsRoot>(txt).questions
        } catch (t: Throwable) {
            emptyList()
        }
    }

    fun loadRecipes(): List<Recipe> {
        return try {
            val txt = readAsset("recipes_en.json")
            json.decodeFromString<RecipesRoot>(txt).recipes
        } catch (t: Throwable) {
            emptyList()
        }
    }

    fun loadMarket(): List<MarketItem> {
        return try {
            val txt = readAsset("market_restaurant_en.json")
            json.decodeFromString<MarketRestaurantRoot>(txt).marketItems
        } catch (t: Throwable) {
            emptyList()
        }
    }

    fun loadRestaurants(): List<RestaurantItem> {
        return try {
            val txt = readAsset("market_restaurant_en.json")
            json.decodeFromString<MarketRestaurantRoot>(txt).restaurantItems
        } catch (t: Throwable) {
            emptyList()
        }
    }
}
