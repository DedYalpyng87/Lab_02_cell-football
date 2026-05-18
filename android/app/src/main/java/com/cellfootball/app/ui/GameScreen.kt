package com.cellfootball.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cellfootball.app.game.FieldSpec
import com.cellfootball.app.game.GameMode
import com.cellfootball.app.game.GridPoint
import com.cellfootball.app.game.MoveEngine
import com.cellfootball.app.game.PlayerSide
import com.cellfootball.app.game.afterHumanMove
import com.cellfootball.app.game.canConfirmMove
import com.cellfootball.app.game.confirmMove
import com.cellfootball.app.game.newMatch
import com.cellfootball.app.game.tryAppendStep

private val BottomPanelHeight = 196.dp
private val FieldAspect = FieldSpec.WIDTH.toFloat() / FieldSpec.LENGTH

@Composable
fun GameScreen(
    mode: GameMode?,
    modifier: Modifier = Modifier,
    onBackToMenu: () -> Unit
) {
    var match by remember { mutableStateOf(newMatch()) }
    var dragPreview by remember { mutableStateOf<GridPoint?>(null) }

    val isAiTurn = mode == GameMode.VsAi && match.currentPlayer == PlayerSide.Two
    val inputEnabled = !isAiTurn

    val turnName = when {
        isAiTurn -> "ИИ"
        else -> match.currentPlayer.label
    }
    val statusLine = when (mode) {
        GameMode.VsAi -> "Против ИИ · ход: $turnName"
        GameMode.LocalPvp -> "Ход: $turnName"
        null -> "Режим не выбран"
    }

    val segmentsDone = match.moveStep.coerceAtMost(MoveEngine.MOVE_SEGMENTS)
    val hintLine = when {
        isAiTurn -> "Ход ИИ…"
        match.moveInProgress.isEmpty() ->
            "Тап или ведите палец к узлу · 3 шага · затем «Подтвердить»"
        match.canConfirmMove() ->
            "Готово: 3 шага — нажмите «Подтвердить»"
        else ->
            "Шаг $segmentsDone из ${MoveEngine.MOVE_SEGMENTS} · ведите к следующему узлу"
    }

    fun appendStep(node: GridPoint) {
        val last = match.moveInProgress.lastOrNull()
        if (node != last) {
            match = match.tryAppendStep(node)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = statusLine,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        MatchScoreBoard(
            scoreP1 = match.scoreP1,
            scoreP2 = match.scoreP2,
            mode = mode,
            currentPlayer = match.currentPlayer,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            val fieldHeight = minOf(maxHeight, maxWidth / FieldAspect)
            val fieldWidth = fieldHeight * FieldAspect
            Box(
                modifier = Modifier
                    .size(fieldWidth, fieldHeight)
                    .align(Alignment.Center)
            ) {
                GameFieldCanvas(
                    state = match,
                    dragPreviewNode = dragPreview,
                    enabled = inputEnabled,
                    onNodeTapped = { appendStep(it) },
                    onDragHover = { dragPreview = it },
                    onDragFinished = { node ->
                        dragPreview = null
                        node?.let { appendStep(it) }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(BottomPanelHeight)
        ) {
            Text(
                text = hintLine,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
            Button(
                onClick = {
                    match = match.confirmMove().afterHumanMove(mode)
                },
                enabled = inputEnabled && match.canConfirmMove(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Подтвердить")
            }
            OutlinedButton(
                onClick = {
                    dragPreview = null
                    match = MoveEngine.cancelMove(match)
                },
                enabled = inputEnabled && match.moveInProgress.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            ) {
                Text("Отменить ход")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        dragPreview = null
                        match = newMatch()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                ) {
                    Text("Новая партия")
                }
                Button(
                    onClick = onBackToMenu,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("В меню")
                }
            }
        }
    }
}
