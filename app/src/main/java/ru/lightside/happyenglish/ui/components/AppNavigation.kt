package ru.lightside.happyenglish.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.lightside.happyenglish.ui.screen.AddWordScreen
import ru.lightside.happyenglish.ui.screen.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_screen") {

        // Главный экран
        composable("main_screen") {
            MainScreen(
                onNavigateToAddWord = { navController.navigate("add_word_screen") }
            )
        }

        // Экран добавления слов
        composable("add_word_screen") {
            AddWordScreen(onBack = { navController.popBackStack() })
        }
    }
}
