package ru.lightside.happyenglish.ui.effects

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun ShakeEffect(
    isShaking: Boolean,
    onAnimationFinished: () -> Unit,
    content: @Composable () -> Unit
) {
    // Анимация смещения: значение будет меняться от -10.dp до 10.dp
    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(isShaking) {
        if (isShaking) {
            // Быстрая серия движений
            for (i in 0..4) {
                offsetX.animateTo(10f, animationSpec = tween(50, easing = LinearEasing))
                offsetX.animateTo(-10f, animationSpec = tween(50, easing = LinearEasing))
            }
            // Возврат в центр
            offsetX.animateTo(0f, animationSpec = tween(50))

            // Сообщаем родителю, что анимация завершена (чтобы сбросить флаг ошибки)
            onAnimationFinished()
        }
    }

    Box(
        modifier = Modifier.offset { IntOffset(offsetX.value.roundToInt(), 0) }
    ) {
        content()
    }
}
