package com.moodmeal.mixer.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.moodmeal.mixer.data.*
import com.moodmeal.mixer.domain.Engine

class AppViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repo = Repo(application)
    
    // Data pools
    var allQuestions by mutableStateOf<List<MoodQuestion>>(emptyList())
        private set
    var allRecipes by mutableStateOf<List<Recipe>>(emptyList())
        private set
    var allMarkets by mutableStateOf<List<MarketItem>>(emptyList())
        private set
    var allRestaurants by mutableStateOf<List<RestaurantItem>>(emptyList())
        private set
    
    // User preferences
    var animationsOn by mutableStateOf(true)
    var isVegan by mutableStateOf(false)
    var isVegetarian by mutableStateOf(false)
    var isHalal by mutableStateOf(false)
    var isGlutenFree by mutableStateOf(false)
    
    // Current session
    var preTestQuestions by mutableStateOf<List<MoodQuestion>>(emptyList())
        private set
    var preAnswers by mutableStateOf<List<Int>>(emptyList())
    var preScores by mutableStateOf(Scores())
        private set
    var currentSuggestion by mutableStateOf(Suggestion(null, null, null))
        private set
    
    var postTestQuestions by mutableStateOf<List<MoodQuestion>>(emptyList())
        private set
    var postAnswers by mutableStateOf<List<Int>>(emptyList())
    var postScores by mutableStateOf(Scores())
        private set
    var currentMUS by mutableStateOf(0.0)
        private set
    
    // History
    var history by mutableStateOf<List<Session>>(emptyList())
        private set
    
    init {
        loadData()
    }
    
    private fun loadData() {
        allQuestions = repo.loadQuestions()
        allRecipes = repo.loadRecipes()
        allMarkets = repo.loadMarket()
        allRestaurants = repo.loadRestaurants()
    }
    
    fun newPreTest() {
        preTestQuestions = Engine.pickFive(allQuestions)
        preAnswers = List(5) { 3 } // Default to middle value
    }
    
    fun updatePreAnswer(index: Int, value: Int) {
        preAnswers = preAnswers.toMutableList().apply { 
            if (index in indices) this[index] = value 
        }
    }
    
    fun computePre() {
        preScores = Engine.score(preAnswers, preTestQuestions)
        currentSuggestion = Engine.suggest(preScores, allRecipes, allMarkets, allRestaurants)
    }
    
    fun newPostTest() {
        // For MVP, pick another 5 questions (preferably different)
        val remaining = allQuestions.filter { it !in preTestQuestions }
        postTestQuestions = if (remaining.size >= 5) {
            Engine.pickFive(remaining)
        } else {
            Engine.pickFive(allQuestions)
        }
        postAnswers = List(5) { 3 }
    }
    
    fun updatePostAnswer(index: Int, value: Int) {
        postAnswers = postAnswers.toMutableList().apply { 
            if (index in indices) this[index] = value 
        }
    }
    
    fun computePost() {
        postScores = Engine.score(postAnswers, postTestQuestions)
        currentMUS = Engine.mus(preScores, postScores)
    }
    
    fun finalize() {
        val session = Session(
            preAnswers = preAnswers,
            preScores = preScores.copy(),
            suggestion = currentSuggestion,
            postAnswers = postAnswers,
            postScores = postScores.copy(),
            mus = currentMUS,
            ts = System.currentTimeMillis()
        )
        history = listOf(session) + history
    }
    
    private fun Scores.copy() = Scores(
        valence = valence,
        arousal = arousal,
        stress = stress,
        craving = craving,
        bodysense = bodysense
    )
}
