package com.cellfootball.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cellfootball.app.game.GameMode
import com.cellfootball.app.ui.GameScreen

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
    Menu, ModeSelection, Game, Tournament, Rules
}

@Composable
fun CellFootballApp() {
    var screen by remember { mutableStateOf(Screen.Menu) }
    var selectedMode by remember { mutableStateOf<GameMode?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (screen) {
            Screen.Menu -> MenuScreen(
                modifier = Modifier.padding(innerPadding),
                onPlay = { screen = Screen.ModeSelection },
                onRules = { screen = Screen.Rules }
            )
            Screen.ModeSelection -> ModeSelectionScreen(
                modifier = Modifier.padding(innerPadding),
                onVsAi = {
                    selectedMode = GameMode.VsAi
                    screen = Screen.Game
                },
                onLocalPvp = {
                    selectedMode = GameMode.LocalPvp
                    screen = Screen.Game
                },
                onTournament = { screen = Screen.Tournament },
                onBack = { screen = Screen.Menu }
            )
            Screen.Game -> GameScreen(
                modifier = Modifier.padding(innerPadding),
                mode = selectedMode,
                onBackToMenu = { screen = Screen.Menu }
            )
            Screen.Tournament -> TournamentPlaceholderScreen(
                modifier = Modifier.padding(innerPadding),
                onBack = { screen = Screen.ModeSelection }
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
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.pole),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Клеточный футбол",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFFFF9800)
            )
            Text(
                "MVP v0 — ЛР 5",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
            Button(onClick = onPlay, modifier = Modifier.padding(top = 56.dp)) {
                Text("Играть")
            }
            Button(onClick = onRules, modifier = Modifier.padding(top = 12.dp)) {
                Text("Правила")
            }
        }
    }
}

@Composable
fun ModeSelectionScreen(
    modifier: Modifier = Modifier,
    onVsAi: () -> Unit,
    onLocalPvp: () -> Unit,
    onTournament: () -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Выбор режима",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Button(onClick = onVsAi, modifier = Modifier.padding(top = 24.dp)) {
                Text("Против ИИ")
            }
            Button(onClick = onLocalPvp, modifier = Modifier.padding(top = 12.dp)) {
                Text("Два игрока")
            }
            Button(onClick = onTournament, modifier = Modifier.padding(top = 12.dp)) {
                Text("Турнир")
            }
            Button(onClick = onBack, modifier = Modifier.padding(top = 24.dp)) {
                Text("Назад")
            }
        }
    }
}

@Composable
fun TournamentPlaceholderScreen(
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
        Text("Турнир", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Заглушка: участники и сетка — позже",
            modifier = Modifier.padding(top = 8.dp)
        )
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
    val context = LocalContext.current
    val rulesText = remember {
        AssetTextLoader.load(context, "game-rules.md")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            item {
                MarkdownText(
                    markdown = rulesText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Назад")
        }
    }
}