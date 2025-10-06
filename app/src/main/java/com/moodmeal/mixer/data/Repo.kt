package com.moodmeal.mixer.data

import android.content.Context
import kotlinx.serialization.json.Json
import java.io.BufferedReader

class Repo(private val context: Context) {
    
    private val json = Json { 
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    fun loadQuestions(): List<MoodQuestion> {
        return try {
            val jsonString = context.assets.open("questions_en.json")
                .bufferedReader()
                .use(BufferedReader::readText)
            val data = json.decodeFromString<QuestionsList>(jsonString)
            data.questions
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    fun loadRecipes(): List<Recipe> {
        return try {
            val jsonString = context.assets.open("recipes_en.json")
                .bufferedReader()
                .use(BufferedReader::readText)
            val data = json.decodeFromString<RecipesList>(jsonString)
            data.recipes
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    fun loadMarket(): List<MarketItem> {
        return try {
            val jsonString = context.assets.open("market_restaurant_en.json")
                .bufferedReader()
                .use(BufferedReader::readText)
            val data = json.decodeFromString<MarketRestaurantData>(jsonString)
            data.marketItems
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    fun loadRestaurants(): List<RestaurantItem> {
        return try {
            val jsonString = context.assets.open("market_restaurant_en.json")
                .bufferedReader()
                .use(BufferedReader::readText)
            val data = json.decodeFromString<MarketRestaurantData>(jsonString)
            data.restaurantItems
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
