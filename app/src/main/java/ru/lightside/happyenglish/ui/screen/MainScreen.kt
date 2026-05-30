package ru.lightside.happyenglish.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.lightside.happyenglish.ui.components.AppHeader
import ru.lightside.happyenglish.ui.components.MainNavButtons
import ru.lightside.happyenglish.ui.theme.Purple80
import ru.lightside.happyenglish.viewmodels.GameViewModel

@Composable
fun MainScreen(
    onNavigateToAddWord: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Purple80
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AppHeader()

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                GameScreen(onNavigateToAddWord = onNavigateToAddWord, viewModel = viewModel)
            }
            Spacer(modifier = Modifier.height(12.dp))
            MainNavButtons(
                onNavigateToAddWord = onNavigateToAddWord,
                onStartGame = { viewModel.switchToGame() }
            )
        }
    }
}
