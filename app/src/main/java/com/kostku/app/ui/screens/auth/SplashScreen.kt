package com.kostku.app.ui.screens.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostku.app.ui.components.KostKuLogo
import com.kostku.app.ui.components.ScreenContainer
import com.kostku.app.ui.theme.Green900
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.WordmarkFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val logoScale = remember { Animatable(0.55f) }
    val logoAlpha = remember { Animatable(0f) }
    val textProgress = remember { Animatable(0f) }
    val currentOnFinished by rememberUpdatedState(onFinished)

    LaunchedEffect(Unit) {
        coroutineScope {
            launch { logoAlpha.animateTo(1f, tween(500)) }
            launch {
                logoScale.animateTo(
                    1f,
                    spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                )
            }
            launch {
                delay(450)
                textProgress.animateTo(1f, tween(650, easing = FastOutSlowInEasing))
            }
        }
        delay(700)
        currentOnFinished()
    }

    ScreenContainer {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KostKuLogo(
                size = 132.dp,
                modifier = Modifier.graphicsLayer {
                    scaleX = logoScale.value
                    scaleY = logoScale.value
                    alpha = logoAlpha.value
                }
            )
            Spacer(Modifier.height(4.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    alpha = textProgress.value
                    translationY = (1f - textProgress.value) * 18.dp.toPx()
                }
            ) {
                Text(
                    text = "KostKu",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontFamily = WordmarkFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 44.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = Green900
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Manajemen Kost Modern New Afra Kost",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Ink600,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}
