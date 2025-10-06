package com.example.moodmeal.domain

import com.example.moodmeal.data.*
import kotlin.random.Random

object Engine {
    fun pickFive(pool: List<MoodQuestion>): List<MoodQuestion> {
        if (pool.size <= 5) return pool.shuffled().take(5)
        val byDim = pool.groupBy { it.dim }
        val dims = MoodDim.values().toList()
        val selected = mutableListOf<MoodQuestion>()
        val used = mutableSetOf<String>()
        // First pass: try to pick one per dimension
        for (dim in dims) {
            val candidates = byDim[dim]?.filter { it.id !in used }?.shuffled() ?: emptyList()
            if (candidates.isNotEmpty()) {
                val q = candidates.first()
                selected.add(q)
                used.add(q.id)
                if (selected.size == 5) return selected
            }
        }
        // Fill remaining
        val remaining = pool.filter { it.id !in used }.shuffled()
        selected.addAll(remaining.take(5 - selected.size))
        return selected
    }

    fun score(answers: List<Int>, questions: List<MoodQuestion>): Scores {
        val clamped = answers.map { it.coerceIn(1, 5) }
        val byDimValues = mutableMapOf<MoodDim, MutableList<Double>>()
        for (i in questions.indices) {
            val dim = questions[i].dim
            val value = clamped.getOrElse(i) { 3 }
            val norm = (value - 1) / 4.0 // 1..5 -> 0..1
            byDimValues.getOrPut(dim) { mutableListOf() }.add(norm)
        }
        fun mean(list: List<Double>): Double = if (list.isEmpty()) 0.5 else list.average()
        return Scores(
            valence = mean(byDimValues[MoodDim.valence] ?: emptyList()),
            arousal = mean(byDimValues[MoodDim.arousal] ?: emptyList()),
            stress = mean(byDimValues[MoodDim.stress] ?: emptyList()),
            craving = mean(byDimValues[MoodDim.craving] ?: emptyList()),
            bodysense = mean(byDimValues[MoodDim.bodysense] ?: emptyList()),
        )
    }

    fun suggest(
        scores: Scores,
        recipes: List<Recipe>,
        markets: List<MarketItem>,
        restos: List<RestaurantItem>,
    ): Suggestion {
        var recipe: Recipe? = recipes.firstOrNull()
        var market: MarketItem? = markets.firstOrNull()
        var resto: RestaurantItem? = restos.firstOrNull()

        // Simple rule-based selection
        if (scores.stress >= 0.6) {
            recipe = recipes.firstOrNull { it.name.contains("lentil", ignoreCase = true) || it.name.contains("soup", ignoreCase = true) } ?: recipe
            market = markets.firstOrNull() ?: market
            resto = restos.firstOrNull() ?: resto
        } else if (scores.arousal >= 0.6) {
            recipe = recipes.firstOrNull { it.name.contains("salad", ignoreCase = true) || it.name.contains("tuna", ignoreCase = true) } ?: recipe
            market = markets.drop(1).firstOrNull() ?: market // market last -> pick not-first if available
            resto = restos.firstOrNull() ?: resto
        }

        return Suggestion(recipe = recipe, market = market, restaurant = resto)
    }

    fun mus(pre: Scores, post: Scores): Double {
        return (post.valence + post.arousal) - (pre.valence + pre.arousal)
    }
}
