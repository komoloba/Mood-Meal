package com.example.moodmeal.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.moodmeal.data.*
import com.example.moodmeal.domain.Engine

class AppViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repo(app)

    // Flags and data
    var animationsOn by mutableStateOf(true)
        private set
    var dietFlags by mutableStateOf(setOf<String>())
        private set

    // Pools
    val questionsPool: List<MoodQuestion> = repo.loadQuestions()
    val recipes: List<Recipe> = repo.loadRecipes()
    val markets: List<MarketItem> = repo.loadMarket()
    val restos: List<RestaurantItem> = repo.loadRestaurants()

    // In-flight state
    var currentPreQuestions by mutableStateOf<List<MoodQuestion>>(emptyList())
        private set
    var currentPreAnswers by mutableStateOf<List<Int>>(emptyList())
        private set

    var currentSuggestion by mutableStateOf(Suggestion(null, null, null))
        private set

    var currentPostQuestions by mutableStateOf<List<MoodQuestion>>(emptyList())
        private set
    var currentPostAnswers by mutableStateOf<List<Int>>(emptyList())
        private set

    var history by mutableStateOf<List<Session>>(emptyList())
        private set

    fun setFlags(animations: Boolean, diet: Set<String>) {
        animationsOn = animations
        dietFlags = diet
    }

    fun newPreTest() {
        val picked = Engine.pickFive(questionsPool)
        currentPreQuestions = picked
        currentPreAnswers = List(picked.size) { 3 }
        currentSuggestion = Suggestion(null, null, null)
        currentPostQuestions = emptyList()
        currentPostAnswers = emptyList()
    }

    fun updatePreAnswer(index: Int, value: Int) {
        currentPreAnswers = currentPreAnswers.toMutableList().also { list ->
            if (index in list.indices) list[index] = value
        }
    }

    fun updatePostAnswer(index: Int, value: Int) {
        currentPostAnswers = currentPostAnswers.toMutableList().also { list ->
            if (index in list.indices) list[index] = value
        }
    }

    fun computePre() {
        val scores = Engine.score(currentPreAnswers, currentPreQuestions)
        val suggestion = Engine.suggest(scores, recipes, markets, restos)
        currentSuggestion = suggestion
        // Prepare post-test questions (simple random or prefer different items)
        val pool = questionsPool.shuffled()
        val picked = Engine.pickFive(pool)
        currentPostQuestions = picked
        currentPostAnswers = List(picked.size) { 3 }
    }

    fun finalizeSession(): Session {
        val preScores = Engine.score(currentPreAnswers, currentPreQuestions)
        val postScores = Engine.score(currentPostAnswers, currentPostQuestions)
        val mus = Engine.mus(preScores, postScores)
        val session = Session(
            preAnswers = currentPreAnswers,
            preScores = preScores,
            suggestion = currentSuggestion,
            postAnswers = currentPostAnswers,
            postScores = postScores,
            mus = mus,
            ts = System.currentTimeMillis(),
        )
        history = listOf(session) + history
        return session
    }
}
