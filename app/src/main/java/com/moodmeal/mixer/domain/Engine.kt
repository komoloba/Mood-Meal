package com.moodmeal.mixer.domain

import com.moodmeal.mixer.data.*
import kotlin.random.Random

object Engine {
    
    /**
     * Pick 5 questions stratified by dimension where possible
     */
    fun pickFive(pool: List<MoodQuestion>): List<MoodQuestion> {
        if (pool.size <= 5) return pool
        
        val grouped = pool.groupBy { it.dim }
        val picked = mutableListOf<MoodQuestion>()
        
        // Try to get one from each dimension
        MoodDim.values().forEach { dim ->
            val dimQuestions = grouped[dim.name]
            if (!dimQuestions.isNullOrEmpty() && picked.size < 5) {
                picked.add(dimQuestions.random())
            }
        }
        
        // If we need more, add random ones
        while (picked.size < 5 && picked.size < pool.size) {
            val candidate = pool.random()
            if (candidate !in picked) {
                picked.add(candidate)
            }
        }
        
        return picked.shuffled()
    }
    
    /**
     * Score answers (1..5 Likert) to normalized 0..1 per dimension
     */
    fun score(answers: List<Int>, questions: List<MoodQuestion>): Scores {
        val scores = Scores()
        val dimAnswers = mutableMapOf<String, MutableList<Double>>()
        
        questions.forEachIndexed { idx, q ->
            if (idx < answers.size) {
                val normalized = (answers[idx] - 1) / 4.0 // 1..5 -> 0..1
                dimAnswers.getOrPut(q.dim) { mutableListOf() }.add(normalized)
            }
        }
        
        // Compute means per dimension
        dimAnswers["valence"]?.average()?.let { scores.valence = it }
        dimAnswers["arousal"]?.average()?.let { scores.arousal = it }
        dimAnswers["stress"]?.average()?.let { scores.stress = it }
        dimAnswers["craving"]?.average()?.let { scores.craving = it }
        dimAnswers["bodysense"]?.average()?.let { scores.bodysense = it }
        
        return scores
    }
    
    /**
     * Simple rule-based suggestion matching
     */
    fun suggest(
        scores: Scores,
        recipes: List<Recipe>,
        markets: List<MarketItem>,
        restaurants: List<RestaurantItem>
    ): Suggestion {
        
        var recipe: Recipe? = null
        var market: MarketItem? = null
        var restaurant: RestaurantItem? = null
        
        // High stress -> prefer soup/warm/soothing
        if (scores.stress >= 0.6) {
            recipe = recipes.firstOrNull { r ->
                r.tags.moods.any { it.contains("stress", ignoreCase = true) }
                    || r.name.contains("soup", ignoreCase = true)
                    || r.name.contains("lentil", ignoreCase = true)
            } ?: recipes.firstOrNull()
            
            market = markets.firstOrNull()
            restaurant = restaurants.firstOrNull()
        }
        // High arousal -> prefer fresh/light
        else if (scores.arousal >= 0.6) {
            recipe = recipes.firstOrNull { r ->
                r.tags.moods.any { it.contains("energetic", ignoreCase = true) }
                    || r.name.contains("salad", ignoreCase = true)
                    || r.name.contains("tuna", ignoreCase = true)
            } ?: recipes.lastOrNull()
            
            market = markets.lastOrNull() ?: markets.firstOrNull()
            restaurant = restaurants.firstOrNull()
        }
        // Default fallback
        else {
            recipe = recipes.firstOrNull()
            market = markets.firstOrNull()
            restaurant = restaurants.firstOrNull()
        }
        
        return Suggestion(recipe, market, restaurant)
    }
    
    /**
     * Mood Uplift Score = (post.valence + post.arousal) - (pre.valence + pre.arousal)
     */
    fun mus(pre: Scores, post: Scores): Double {
        return (post.valence + post.arousal) - (pre.valence + pre.arousal)
    }
}
