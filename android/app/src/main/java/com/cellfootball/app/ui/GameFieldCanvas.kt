package com.cellfootball.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.cellfootball.app.game.FieldSpec
import com.cellfootball.app.game.GridPoint
import com.cellfootball.app.game.MatchState
import com.cellfootball.app.game.PlayerColors
import com.cellfootball.app.game.canAppendTo
import com.cellfootball.app.game.pathAnchor

@Composable
fun GameFieldCanvas(
    state: MatchState,
    dragPreviewNode: GridPoint?,
    enabled: Boolean,
    onNodeTapped: (GridPoint) -> Unit,
    onDragHover: (GridPoint?) -> Unit,
    onDragFinished: (GridPoint?) -> Unit,
    modifier: Modifier = Modifier
) {
    val previewPlayer = state.currentPlayer

    Canvas(
        modifier = modifier.fillMaxSize()
            .pointerInput(enabled, state.moveInProgress) {
                if (!enabled) return@pointerInput

                detectTapGestures { offset ->
                    val layout = FieldLayout.fromCanvasSize(size)
                    layout.nearestNode(offset)?.let(onNodeTapped)
                }
            }
            .pointerInput(enabled, state.moveInProgress) {
                if (!enabled) return@pointerInput

                var lastHover: GridPoint? = null
                detectDragGestures(
                    onDrag = { change, _ ->
                        val layout = FieldLayout.fromCanvasSize(size)
                        lastHover = layout.nearestNode(change.position)
                        onDragHover(lastHover)
                    },
                    onDragEnd = {
                        onDragFinished(lastHover)
                        lastHover = null
                        onDragHover(null)
                    },
                    onDragCancel = {
                        lastHover = null
                        onDragHover(null)
                    }
                )
            }
    ) {
        val layout = FieldLayout.fromCanvasSize(size)
        fun node(col: Int, row: Int): Offset = layout.nodeOffset(col, row)
        fun nodeAt(p: GridPoint): Offset = node(p.col, p.row)

        drawRect(
            color = Color(0xFF2E7D32),
            topLeft = node(0, 0),
            size = androidx.compose.ui.geometry.Size(
                node(FieldSpec.WIDTH - 1, 0).x - node(0, 0).x,
                node(0, FieldSpec.LENGTH - 1).y - node(0, 0).y
            )
        )

        val goalColor = Color(0xFF1B5E20)
        listOf(0, FieldSpec.LENGTH - 1).forEach { goalRow ->
            val left = node(FieldSpec.goalStartCol, goalRow)
            val right = node(FieldSpec.goalEndCol, goalRow)
            val band = layout.stepY * 0.35f
            drawRect(
                color = goalColor,
                topLeft = Offset(left.x - layout.stepX * 0.2f, left.y - band),
                size = androidx.compose.ui.geometry.Size(
                    right.x - left.x + layout.stepX * 0.4f,
                    band * 2f
                )
            )
        }

        val gridColor = Color(0xFFE8F5E9).copy(alpha = 0.55f)
        for (row in 0 until FieldSpec.LENGTH) {
            for (col in 0 until FieldSpec.WIDTH - 1) {
                drawLine(
                    color = gridColor,
                    start = node(col, row),
                    end = node(col + 1, row),
                    strokeWidth = 1.2f
                )
            }
        }
        for (col in 0 until FieldSpec.WIDTH) {
            for (row in 0 until FieldSpec.LENGTH - 1) {
                drawLine(
                    color = gridColor,
                    start = node(col, row),
                    end = node(col, row + 1),
                    strokeWidth = 1.2f
                )
            }
        }

        drawLine(
            color = Color.White.copy(alpha = 0.85f),
            start = node(0, FieldSpec.centerRow),
            end = node(FieldSpec.WIDTH - 1, FieldSpec.centerRow),
            strokeWidth = 2.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))
        )

        val goalLineColor = Color.White.copy(alpha = 0.9f)
        listOf(0, FieldSpec.LENGTH - 1).forEach { goalRow ->
            drawLine(
                color = goalLineColor,
                start = node(FieldSpec.goalStartCol, goalRow),
                end = node(FieldSpec.goalEndCol, goalRow),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }

        state.lines.forEach { segment ->
            drawLine(
                color = PlayerColors.lineColor(segment.player),
                start = node(segment.from.col, segment.from.row),
                end = node(segment.to.col, segment.to.row),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
        }

        val path = state.moveInProgress
        val previewColor = PlayerColors.lineColor(previewPlayer)
        if (path.size >= 2) {
            for (i in 0 until path.size - 1) {
                val a = path[i]
                val b = path[i + 1]
                drawLine(
                    color = previewColor.copy(alpha = 0.85f),
                    start = node(a.col, a.row),
                    end = node(b.col, b.row),
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )
            }
        }

        dragPreviewNode?.let { target ->
            val anchor = state.pathAnchor().last()
            val valid = state.canAppendTo(target)
            val lineColor = if (valid) previewColor else Color(0xFFEF5350)
            drawLine(
                color = lineColor.copy(alpha = 0.85f),
                start = nodeAt(anchor),
                end = nodeAt(target),
                strokeWidth = 5f,
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f))
            )
            drawCircle(
                color = lineColor.copy(alpha = 0.35f),
                radius = layout.stepX.coerceAtMost(layout.stepY) * 0.22f,
                center = nodeAt(target)
            )
        }

        val dotRadius = layout.stepX.coerceAtMost(layout.stepY) * 0.12f
        for (row in 0 until FieldSpec.LENGTH) {
            for (col in 0 until FieldSpec.WIDTH) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.35f),
                    radius = dotRadius,
                    center = node(col, row)
                )
            }
        }

        val displayBall = path.lastOrNull() ?: state.ball
        val ballCenter = nodeAt(displayBall)
        drawCircle(
            color = Color(0xFFFF5722),
            radius = dotRadius * 1.8f,
            center = ballCenter
        )
        drawCircle(
            color = Color.White,
            radius = dotRadius * 1.8f,
            center = ballCenter,
            style = Stroke(width = 2f)
        )
    }
}
