package ru.lightside.relaxpaint.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.lightside.relaxpaint.R
import ru.lightside.relaxpaint.ui.theme.Pink40
import ru.lightside.relaxpaint.ui.theme.Purple40
import ru.lightside.relaxpaint.ui.theme.RelaxPaintTheme


// todo указать авторов шрифта где-нибудь Vasily Shishkin
// Дмитрий Антонов / Синяя Кривая Студия Дизайна
@Composable
fun AppHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Happy English",
            fontSize = 28.sp,
            color = Pink40,
            fontFamily = FontFamily(Font(R.font.farabee_regular))
        )
        Text(
            text = "Учим английские слова",
            fontSize = 16.sp,
            color = Purple40,
            fontFamily = FontFamily(Font(R.font.jun_regular))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppHeaderPreview() {
    RelaxPaintTheme {
        AppHeader()
    }
}
