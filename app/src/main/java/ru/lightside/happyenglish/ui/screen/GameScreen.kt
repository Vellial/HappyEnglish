package ru.lightside.happyenglish.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun GameScreen(
    onNavigateToAddWord: () -> Unit = {},
    viewModel: GameViewModel = hiltViewModel()
) {
    val originals by viewModel.shuffledOriginals.collectAsState()
    val translations by viewModel.shuffledTranslations.collectAsState()
    val gameState by viewModel.gameState.collectAsState()
    val timer by viewModel.timer.collectAsState()
    val progress by viewModel.playerProgress.collectAsState()
    val isTrainingMode by viewModel.isTrainingMode.collectAsState()
    val levelData = viewModel.currentLevelData

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isTrainingMode) {
                Text(
                    text = "Тренировка",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                Column {
                    Text(text = "Уровень ${progress.currentLevel}: ${levelData.name}", fontSize = 14.sp, style = MaterialTheme.typography.titleSmall)
                    Text(text = "Монеты: ${progress.coins} | XP: ${progress.xp}", fontSize = 14.sp)
                }
            }
            if (!isTrainingMode && (timer > 0 || gameState == GameState.Playing)) {
                Text(
                    text = if (levelData.timeLimitSeconds != null) "⏱ $timer" else "",
                    fontSize = 20.sp,
                    color = if (timer < 10) Color.Red else Color.Black,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 150.dp)
                .padding(horizontal = 4.dp)
                .background(
                    color = Color.White.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val maxItems = if (originals.size >= translations.size) originals.size else translations.size
                for (i in 0 until maxItems) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (i < originals.size) {
                            WordCard(
                                modifier = Modifier.weight(1f),
                                text = originals[i].original,
                                isSelected = viewModel.selectedOriginalId == originals[i].id,
                                isMatched = false,
                                isError = viewModel.errorIds.contains(originals[i].id),
                                onClick = { viewModel.onSelectionChanged(originals[i].id, true) }
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                        if (i < translations.size) {
                            WordCard(
                                modifier = Modifier.weight(1f),
                                text = translations[i].translation,
                                isSelected = viewModel.selectedTranslationId == translations[i].id,
                                isMatched = false,
                                isError = viewModel.errorIds.contains(translations[i].id),
                                onClick = { viewModel.onSelectionChanged(translations[i].id, false) }
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }

            if (gameState == GameState.Idle && originals.isEmpty()) {
                Box(
                    modifier = Modifier.matchParentSize().background(Color.White.copy(alpha = 0.85f)).padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Нет слов!",
                            fontSize = 28.sp,
                            color = Color(0xFF9C27B0),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "Добавьте новые слова, чтобы начать",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Button(
                            onClick = onNavigateToAddWord,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(text = "Добавить слова")
                        }
                    }
                }
            } else if (gameState == GameState.Finished) {
                Box(
                    modifier = Modifier.matchParentSize().background(Color.White.copy(alpha = 0.85f)).padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Тренировка завершена!",
                            fontSize = 28.sp,
                            color = Color(0xFF4CAF50),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "Добавьте новые слова, чтобы продолжить",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Button(
                            onClick = onNavigateToAddWord,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(text = "Добавить слова")
                        }
                    }
                }
            } else if (gameState == GameState.LevelCompleted || gameState == GameState.LevelFailed) {
                Box(
                    modifier = Modifier.matchParentSize().background(Color.White.copy(alpha = 0.85f)).padding(16.dp),
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
