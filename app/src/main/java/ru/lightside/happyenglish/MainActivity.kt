package ru.lightside.happyenglish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.lightside.happyenglish.ui.components.AppNavigation
import ru.lightside.happyenglish.ui.theme.HappyEnglishTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyEnglishTheme {
                AppNavigation()
            }
        }
    }
}
