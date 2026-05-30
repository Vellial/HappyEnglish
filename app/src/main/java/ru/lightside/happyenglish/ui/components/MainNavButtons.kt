package ru.lightside.happyenglish.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.lightside.happyenglish.R

@Composable
fun MainNavButtons(onNavigateToAddWord: () -> Unit, onStartGame: () -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 5.dp)
    ) {
        Button(onClick = { onNavigateToAddWord() },
            shape = RoundedCornerShape(13.dp),
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .padding(horizontal = 5.dp)
        ) {
            Text(text = "Словарь",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.farabee_medium)))
        }
        Button(onClick = onStartGame,
            shape = RoundedCornerShape(13.dp),
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .padding(horizontal = 5.dp)
        ) {
            Text(text = "Игра",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.farabee_medium)))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MainNavButtonsPreview() {
    MainNavButtons(onNavigateToAddWord = {}, onStartGame = {})
}
