package com.moodmeal.mixer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moodmeal.mixer.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodMealMixerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MoodMealMixerApp()
                }
            }
        }
    }
}

@Composable
fun MoodMealMixerApp() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    
    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen(
                viewModel = viewModel,
                onNext = { navController.navigate("pre") }
            )
        }
        
        composable("pre") {
            PreTestScreen(
                viewModel = viewModel,
                onNext = { navController.navigate("results") }
            )
        }
        
        composable("results") {
            ResultsScreen(
                viewModel = viewModel,
                onNext = { navController.navigate("post") }
            )
        }
        
        composable("post") {
            PostTestScreen(
                viewModel = viewModel,
                onNext = { navController.navigate("history") }
            )
        }
        
        composable("history") {
            HistoryScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun MoodMealMixerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
