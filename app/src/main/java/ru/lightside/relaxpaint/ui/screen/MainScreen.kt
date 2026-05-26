package ru.lightside.relaxpaint.ui.screen

import androidx.compose.foundation.background
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
import ru.lightside.relaxpaint.ui.components.AppHeader
import ru.lightside.relaxpaint.ui.components.MainNavButtons
import ru.lightside.relaxpaint.ui.theme.Purple80

@Composable
fun MainScreen(onNavigateToAddWord: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Purple80
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AppHeader()

            // Здесь теперь GameScreen, который берет на себя игровую логику
            Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)) {
                GameScreen()
            }

            Spacer(modifier = Modifier.height(30.dp))
            MainNavButtons(onNavigateToAddWord = onNavigateToAddWord)
        }
    }
}
