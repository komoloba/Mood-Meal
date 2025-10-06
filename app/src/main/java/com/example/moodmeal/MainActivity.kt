package com.example.moodmeal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moodmeal.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val vm: AppViewModel = viewModel()

    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "onboarding") {
            composable("onboarding") {
                OnboardingScreen(vm = vm) {
                    vm.newPreTest()
                    navController.navigate("pre")
                }
            }
            composable("pre") {
                PreTestScreen(vm = vm) {
                    vm.computePre()
                    navController.navigate("results")
                }
            }
            composable("results") {
                ResultsScreen(vm = vm) {
                    navController.navigate("post")
                }
            }
            composable("post") {
                PostTestScreen(vm = vm,
                    onSaved = {
                        vm.finalizeSession()
                        navController.navigate("history")
                    }
                )
            }
            composable("history") {
                HistoryScreen(vm = vm)
            }
        }
    }
}
