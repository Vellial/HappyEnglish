package ru.lightside.relaxpaint.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.lightside.relaxpaint.viewmodels.GameViewModel
import ru.lightside.relaxpaint.ui.components.WordCard


@Composable
fun GameScreen(viewModel: GameViewModel = hiltViewModel()) {
    val originals by viewModel.shuffledOriginals.collectAsState()
    val translations by viewModel.shuffledTranslations.collectAsState()
    val matchedIds = viewModel.matchedIds

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Оригинальные слова
        items(
            items = originals.filter { it.id !in matchedIds },
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
            items = translations.filter { it.id !in matchedIds },
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
}
