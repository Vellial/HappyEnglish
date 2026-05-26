package ru.lightside.happyenglish.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.lightside.happyenglish.ui.components.WordCard
import ru.lightside.happyenglish.viewmodels.GameState
import ru.lightside.happyenglish.viewmodels.GameViewModel


@Composable
fun GameScreen(viewModel: GameViewModel = hiltViewModel()) {
    val originals by viewModel.shuffledOriginals.collectAsState()
    val translations by viewModel.shuffledTranslations.collectAsState()
    val gameState by viewModel.gameState.collectAsState()
    val timer by viewModel.timer.collectAsState()
    val progress by viewModel.playerProgress.collectAsState()
    val levelData = viewModel.currentLevelData

    Column(modifier = Modifier.fillMaxSize()) {
        // Статистика и таймер
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Уровень ${progress.currentLevel}: ${levelData.name}", fontSize = 14.sp, style = MaterialTheme.typography.titleSmall)
                Text(text = "Монеты: ${progress.coins} | XP: ${progress.xp}", fontSize = 14.sp)
            }
            if (timer > 0 || gameState == GameState.Playing) {
                Text(
                    text = if (levelData.timeLimitSeconds != null) "⏱ $timer" else "",
                    fontSize = 20.sp,
                    color = if (timer < 10) Color.Red else Color.Black,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Оригинальные слова
                items(
                    items = originals,
                    key = { it.id }
                ) { pair ->
                    WordCard(
                        modifier = Modifier.animateItem(),
                        text = pair.original,
                        isSelected = viewModel.selectedOriginalId == pair.id,
                        isMatched = false,
                        isError = viewModel.errorIds.contains(pair.id),
                        onClick = { viewModel.onSelectionChanged(pair.id, true) }
                    )
                }

                // Переводы
                items(
                    items = translations,
                    key = { "${it.id}_trans" }
                ) { pair ->
                    WordCard(
                        modifier = Modifier.animateItem(),
                        text = pair.translation,
                        isSelected = viewModel.selectedTranslationId == pair.id,
                        isMatched = false,
                        isError = viewModel.errorIds.contains(pair.id),
                        onClick = { viewModel.onSelectionChanged(pair.id, false) }
                    )
                }
            }

            // Оверлеи для состояний игры
            if (gameState == GameState.LevelCompleted || gameState == GameState.LevelFailed) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = if (gameState == GameState.LevelCompleted) "Уровень пройден!" else "Уровень провален!",
                            fontSize = 28.sp,
                            color = if (gameState == GameState.LevelCompleted) Color(0xFF4CAF50) else Color.Red,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        
                        if (gameState == GameState.LevelCompleted) {
                            Text(
                                text = "Награда: +${levelData.coinsReward} 💰 | +${levelData.xpReward} XP",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            Text(
                                text = "Попробуйте еще раз!",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        Button(
                            onClick = { viewModel.continueGame() },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(text = if (gameState == GameState.LevelCompleted) "Следующий уровень" else "Повторить")
                        }
                    }
                }
            }
        }
    }
}
