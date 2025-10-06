package com.moodmeal.mixer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.moodmeal.mixer.data.MarketItem
import com.moodmeal.mixer.data.Recipe
import com.moodmeal.mixer.data.RestaurantItem
import com.moodmeal.mixer.data.Session
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: AppViewModel,
    onNext: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mood-Meal Mixer") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "🐿️ Welcome!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Let's personalize your experience",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Diet preferences
            CheckboxRow(
                label = "🌱 Vegan",
                checked = viewModel.isVegan,
                onCheckedChange = { viewModel.isVegan = it }
            )
            
            CheckboxRow(
                label = "🥬 Vegetarian",
                checked = viewModel.isVegetarian,
                onCheckedChange = { viewModel.isVegetarian = it }
            )
            
            CheckboxRow(
                label = "☪️ Halal",
                checked = viewModel.isHalal,
                onCheckedChange = { viewModel.isHalal = it }
            )
            
            CheckboxRow(
                label = "🌾 Gluten-free",
                checked = viewModel.isGlutenFree,
                onCheckedChange = { viewModel.isGlutenFree = it }
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            CheckboxRow(
                label = "✨ Enable animations",
                checked = viewModel.animationsOn,
                onCheckedChange = { viewModel.animationsOn = it }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    viewModel.newPreTest()
                    onNext()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun CheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreTestScreen(
    viewModel: AppViewModel,
    onNext: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("How do you feel?") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.animationsOn) {
                SquirrelLottie(modifier = Modifier.padding(16.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            viewModel.preTestQuestions.forEachIndexed { index, question ->
                QuestionRow(
                    questionText = question.text,
                    selectedValue = viewModel.preAnswers.getOrNull(index) ?: 3,
                    onValueChange = { viewModel.updatePreAnswer(index, it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = {
                    viewModel.computePre()
                    onNext()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show Suggestions")
            }
        }
    }
}

@Composable
fun QuestionRow(
    questionText: String,
    selectedValue: Int,
    onValueChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = questionText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (value in 1..5) {
                    FilterChip(
                        selected = selectedValue == value,
                        onClick = { onValueChange(value) },
                        label = { Text(value.toString()) }
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Disagree",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Agree",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    viewModel: AppViewModel,
    onNext: () -> Unit
) {
    var portionMultiplier by remember { mutableStateOf(1.0f) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Meal Suggestions") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Based on your mood, here are 3 options:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Recipe card
            viewModel.currentSuggestion.recipe?.let { recipe ->
                RecipeCard(recipe = recipe, portionMultiplier = portionMultiplier)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Market card
            viewModel.currentSuggestion.market?.let { market ->
                MarketCard(market = market, portionMultiplier = portionMultiplier)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Restaurant card
            viewModel.currentSuggestion.restaurant?.let { restaurant ->
                RestaurantCard(restaurant = restaurant, portionMultiplier = portionMultiplier)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Portion slider
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Portion Size: ${String.format("%.1f", portionMultiplier)}×",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = portionMultiplier,
                        onValueChange = { portionMultiplier = it },
                        valueRange = 0.5f..1.5f,
                        steps = 9
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("0.5×", style = MaterialTheme.typography.labelSmall)
                        Text("1.5×", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    viewModel.newPostTest()
                    onNext()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("I ate this → Mini Test")
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, portionMultiplier: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🍳 Cook at Home",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "⏱️ ${recipe.timeMin} min  •  ${(recipe.nutrition.kcalEst * portionMultiplier).toInt()} kcal (${(recipe.nutrition.kcalLow * portionMultiplier).toInt()}-${(recipe.nutrition.kcalHigh * portionMultiplier).toInt()})",
                style = MaterialTheme.typography.bodyMedium
            )
            if (recipe.allergens.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⚠️ ${recipe.allergens.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = { /* Alternative stub */ }) {
                Text("Alternative")
            }
        }
    }
}

@Composable
fun MarketCard(market: MarketItem, portionMultiplier: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🛒 Grab from Market",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = market.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "📊 ${(market.kcalPerPortion * portionMultiplier).toInt()} kcal per portion",
                style = MaterialTheme.typography.bodyMedium
            )
            if (market.allergens.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⚠️ ${market.allergens.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            if (market.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = market.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = { /* Alternative stub */ }) {
                Text("Alternative")
            }
        }
    }
}

@Composable
fun RestaurantCard(restaurant: RestaurantItem, portionMultiplier: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🍽️ Order Nearby",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = restaurant.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "🌍 ${restaurant.cuisine}  •  ${(restaurant.nutrition.kcalEst * portionMultiplier).toInt()} kcal (${(restaurant.nutrition.kcalLow * portionMultiplier).toInt()}-${(restaurant.nutrition.kcalHigh * portionMultiplier).toInt()})",
                style = MaterialTheme.typography.bodyMedium
            )
            if (restaurant.allergens.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⚠️ ${restaurant.allergens.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            if (restaurant.tips.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "💡 ${restaurant.tips}",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = { /* Alternative stub */ }) {
                Text("Alternative")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTestScreen(
    viewModel: AppViewModel,
    onNext: () -> Unit
) {
    var showScore by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quick Check-In") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How do you feel now?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            viewModel.postTestQuestions.forEachIndexed { index, question ->
                QuestionRow(
                    questionText = question.text,
                    selectedValue = viewModel.postAnswers.getOrNull(index) ?: 3,
                    onValueChange = { viewModel.updatePostAnswer(index, it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (!showScore) {
                Button(
                    onClick = {
                        viewModel.computePost()
                        showScore = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Show Score")
                }
            } else {
                // Show MUS badge
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            viewModel.currentMUS > 0.1 -> Color(0xFF4CAF50)
                            viewModel.currentMUS < -0.1 -> Color(0xFFF44336)
                            else -> Color(0xFF9E9E9E)
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when {
                                viewModel.currentMUS > 0.1 -> "↑"
                                viewModel.currentMUS < -0.1 -> "↓"
                                else -> "→"
                            },
                            style = MaterialTheme.typography.displayLarge,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Mood Uplift Score",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(
                            text = String.format("%.2f", viewModel.currentMUS),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                
                Button(
                    onClick = {
                        viewModel.finalize()
                        onNext()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save to History")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: AppViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your History") }
            )
        }
    ) { padding ->
        if (viewModel.history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No sessions yet.\nComplete a mood test to see your history!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.history) { session ->
                    SessionCard(session)
                }
            }
        }
    }
}

@Composable
fun SessionCard(session: Session) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dateFormat.format(Date(session.ts)),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                session.suggestion.recipe?.let {
                    Text(
                        text = "🍳 ${it.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // MUS Badge
            Surface(
                shape = MaterialTheme.shapes.small,
                color = when {
                    session.mus > 0.1 -> Color(0xFF4CAF50)
                    session.mus < -0.1 -> Color(0xFFF44336)
                    else -> Color(0xFF9E9E9E)
                }
            ) {
                Text(
                    text = when {
                        session.mus > 0.1 -> "↑ ${String.format("%.2f", session.mus)}"
                        session.mus < -0.1 -> "↓ ${String.format("%.2f", session.mus)}"
                        else -> "→ ${String.format("%.2f", session.mus)}"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}
