package com.example.moodmeal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moodmeal.data.*
import com.example.moodmeal.domain.Engine

@Composable
fun OnboardingScreen(vm: AppViewModel, onNext: () -> Unit) {
    var vegan by remember { mutableStateOf(false) }
    var vegetarian by remember { mutableStateOf(false) }
    var halal by remember { mutableStateOf(false) }
    var glutenFree by remember { mutableStateOf(false) }
    var animations by remember { mutableStateOf(true) }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Diet preferences")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = vegan, onCheckedChange = { vegan = it })
            Text("Vegan")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = vegetarian, onCheckedChange = { vegetarian = it })
            Text("Vegetarian")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = halal, onCheckedChange = { halal = it })
            Text("Halal")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = glutenFree, onCheckedChange = { glutenFree = it })
            Text("Gluten-free")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = animations, onCheckedChange = { animations = it })
            Text("Enable animations")
        }
        Button(onClick = {
            val set = buildSet {
                if (vegan) add("vegan")
                if (vegetarian) add("vegetarian")
                if (halal) add("halal")
                if (glutenFree) add("gluten-free")
            }
            vm.setFlags(animations, set)
            onNext()
        }) {
            Text("Continue")
        }
    }
}

@Composable
fun PreTestScreen(vm: AppViewModel, onNext: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("How do you feel?", style = MaterialTheme.typography.headlineSmall)
        SquirrelLottie(animationsOn = vm.animationsOn, modifier = Modifier.height(180.dp).fillMaxWidth())
        vm.currentPreQuestions.forEachIndexed { index, q ->
            QuestionRow(text = q.text, value = vm.currentPreAnswers.getOrElse(index) { 3 }, onSelect = {
                vm.updatePreAnswer(index, it)
            })
        }
        Button(onClick = onNext, enabled = vm.currentPreAnswers.size == vm.currentPreQuestions.size) {
            Text("Show Suggestions")
        }
    }
}

@Composable
fun ResultsScreen(vm: AppViewModel, onNext: () -> Unit) {
    var portion by remember { mutableStateOf(1.0f) }
    val s = vm.currentSuggestion

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SuggestionCard(modifier = Modifier.weight(1f), title = "Cook at Home", content = s.recipe?.let { r ->
                "${'$'}{r.name}\n${'$'}{r.timeMin} min\n${'$'}{(r.nutrition.kcalEst * portion).toInt()} kcal (±${'$'}{r.nutrition.kcalHigh - r.nutrition.kcalEst})"
            } ?: "No recipe")
            SuggestionCard(modifier = Modifier.weight(1f), title = "Grab from Market", content = s.market?.let { m ->
                "${'$'}{m.name}\n${'$'}{(m.kcalPerPortion * portion).toInt()} kcal"
            } ?: "No market item")
            SuggestionCard(modifier = Modifier.weight(1f), title = "Order Nearby", content = s.restaurant?.let { re ->
                "${'$'}{re.name} (${ '$' }{re.cuisine})\n${'$'}{(re.nutrition.kcalEst * portion).toInt()} kcal"
            } ?: "No restaurant")
        }
        Text("Portion")
        Slider(value = portion, onValueChange = { portion = it }, valueRange = 0.5f..1.5f)
        Button(onClick = onNext) { Text("I ate this → Mini Test") }
    }
}

@Composable
fun PostTestScreen(vm: AppViewModel, onSaved: () -> Unit) {
    var mus by remember { mutableStateOf<Double?>(null) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Mini Test", style = MaterialTheme.typography.headlineSmall)
        vm.currentPostQuestions.forEachIndexed { index, q ->
            QuestionRow(text = q.text, value = vm.currentPostAnswers.getOrElse(index) { 3 }, onSelect = {
                vm.updatePostAnswer(index, it)
            })
        }
        Button(onClick = {
            val pre = Engine.score(vm.currentPreAnswers, vm.currentPreQuestions)
            val post = Engine.score(vm.currentPostAnswers, vm.currentPostQuestions)
            mus = Engine.mus(pre, post)
        }) { Text("Show Score") }
        mus?.let { value ->
            val label = when {
                value > 0.1 -> "↑ ${'$'}{"%.2f".format(value)}"
                value < -0.1 -> "↓ ${'$'}{"%.2f".format(value)}"
                else -> "→ ${'$'}{"%.2f".format(value)}"
            }
            Text(label, color = when {
                value > 0.1 -> MaterialTheme.colorScheme.primary
                value < -0.1 -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurface
            })
        }
        Button(onClick = onSaved) { Text("Save to History") }
    }
}

@Composable
fun HistoryScreen(vm: AppViewModel) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(vm.history) { s ->
            val mus = s.mus
            val badge = when {
                mus > 0.1 -> "↑"
                mus < -0.1 -> "↓"
                else -> "→"
            }
            Text("${'$'}badge ${'$'}{"%.2f".format(mus)}  on ${'$'}{java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(java.util.Date(s.ts))}")
        }
    }
}

@Composable
fun QuestionRow(text: String, value: Int, onSelect: (Int) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        Text(text)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..5).forEach { v ->
                FilterChip(selected = value == v, onClick = { onSelect(v) }, label = { Text(v.toString()) })
            }
        }
    }
}

@Composable
fun SuggestionCard(modifier: Modifier = Modifier, title: String, content: String) {
    ElevatedCard(modifier) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(content)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { /* TODO alternative stub */ }) { Text("Alternative") }
            }
        }
    }
}
