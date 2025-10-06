package com.example.moodmeal.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*

@Composable
fun SquirrelLottie(animationsOn: Boolean, modifier: Modifier = Modifier) {
    if (!animationsOn) {
        Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("🐿️")
            CircularProgressIndicator()
        }
        return
    }
    val comp by rememberLottieComposition(LottieCompositionSpec.Asset("squirrel_coach.json"))
    val progress by animateLottieCompositionAsState(composition = comp, iterations = LottieConstants.IterateForever)

    if (comp == null) {
        Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("🐿️")
            CircularProgressIndicator()
        }
    } else {
        LottieAnimation(composition = comp, progress = { progress })
    }
}
