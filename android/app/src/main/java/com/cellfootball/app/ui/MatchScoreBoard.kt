package com.cellfootball.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cellfootball.app.game.GameMode
import com.cellfootball.app.game.PlayerSide

private val AccentP1 = Color(0xFF42A5F5)
private val AccentP2 = Color(0xFFFF9800)

@Composable
fun MatchScoreBoard(
    scoreP1: Int,
    scoreP2: Int,
    mode: GameMode?,
    currentPlayer: PlayerSide,
    modifier: Modifier = Modifier
) {
    val nameP1 = PlayerSide.One.label
    val nameP2 = when (mode) {
        GameMode.VsAi -> "ИИ"
        GameMode.LocalPvp, null -> PlayerSide.Two.label
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF1A237E),
                        Color(0xFF303F9F),
                        Color(0xFF1A237E)
                    )
                )
            )
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PlayerLabel(
                name = nameP1,
                accent = AccentP1,
                isActive = currentPlayer == PlayerSide.One,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1.2f)
            ) {
                Text(
                    text = scoreP1.toString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = if (currentPlayer == PlayerSide.One) AccentP1 else Color.White
                )
                Text(
                    text = "·",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.45f)
                )
                Text(
                    text = scoreP2.toString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = if (currentPlayer == PlayerSide.Two) AccentP2 else Color.White
                )
            }
            PlayerLabel(
                name = nameP2,
                accent = AccentP2,
                isActive = currentPlayer == PlayerSide.Two,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PlayerLabel(
    name: String,
    accent: Color,
    isActive: Boolean,
    textAlign: TextAlign,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = if (textAlign == TextAlign.End) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isActive && textAlign == TextAlign.Start) {
            TurnDot(accent)
        }
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isActive) accent else Color.White.copy(alpha = 0.75f),
            textAlign = textAlign,
            maxLines = 1
        )
        if (isActive && textAlign == TextAlign.End) {
            TurnDot(accent)
        }
    }
}

@Composable
private fun TurnDot(color: Color) {
    Box(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .size(7.dp)
            .clip(CircleShape)
            .background(color)
    )
}
