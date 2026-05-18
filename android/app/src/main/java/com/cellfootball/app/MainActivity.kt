package com.cellfootball.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                CellFootballApp()
            }
        }
    }
}

private enum class Screen {
    Menu, Game, Rules
}

@Composable
fun CellFootballApp() {
    var screen by remember { mutableStateOf(Screen.Menu) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (screen) {
            Screen.Menu -> MenuScreen(
                modifier = Modifier.padding(innerPadding),
                onPlay = { screen = Screen.Game },
                onRules = { screen = Screen.Rules }
            )
            Screen.Game -> GamePlaceholderScreen(
                modifier = Modifier.padding(innerPadding),
                onBack = { screen = Screen.Menu }
            )
            Screen.Rules -> RulesScreen(
                modifier = Modifier.padding(innerPadding),
                onBack = { screen = Screen.Menu }
            )
        }
    }
}

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    onPlay: () -> Unit,
    onRules: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Клеточный футбол", style = MaterialTheme.typography.headlineMedium)
        Text("MVP v0 — ЛР 5", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = onPlay, modifier = Modifier.padding(top = 24.dp)) {
            Text("Играть")
        }
        Button(onClick = onRules, modifier = Modifier.padding(top = 12.dp)) {
            Text("Правила")
        }
    }
}

@Composable
fun GamePlaceholderScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Игровой экран", style = MaterialTheme.typography.headlineSmall)
        Text("Заглушка: поле и ходы — позже", modifier = Modifier.padding(top = 8.dp))
        Button(onClick = onBack, modifier = Modifier.padding(top = 24.dp)) {
            Text("Назад")
        }
    }
}

@Composable
fun RulesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Правила (кратко)", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Полный текст — в репозитории: docs/game-rules.md. " +
                    "В приложении позже будет экран со встроенным просмотром.",
            modifier = Modifier.padding(top = 12.dp)
        )
        Button(onClick = onBack, modifier = Modifier.padding(top = 24.dp)) {
            Text("Назад")
        }
    }
}