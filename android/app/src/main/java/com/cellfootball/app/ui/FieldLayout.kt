package com.cellfootball.app.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import com.cellfootball.app.game.FieldSpec
import com.cellfootball.app.game.GridPoint
import kotlin.math.hypot
import kotlin.math.roundToInt

data class FieldLayout(
    val pad: Float,
    val stepX: Float,
    val stepY: Float,
    val width: Float,
    val height: Float
) {
    fun nodeOffset(col: Int, row: Int): Offset =
        Offset(pad + col * stepX, pad + row * stepY)

    fun nearestNode(tap: Offset): GridPoint? {
        if (tap.x < pad - stepX || tap.y < pad - stepY) return null
        if (tap.x > pad + (FieldSpec.WIDTH - 1) * stepX + stepX) return null
        if (tap.y > pad + (FieldSpec.LENGTH - 1) * stepY + stepY) return null

        val col = ((tap.x - pad) / stepX).roundToInt().coerceIn(0, FieldSpec.WIDTH - 1)
        val row = ((tap.y - pad) / stepY).roundToInt().coerceIn(0, FieldSpec.LENGTH - 1)
        val center = nodeOffset(col, row)
        val maxDist = stepX.coerceAtMost(stepY) * 0.55f
        return if (hypot(tap.x - center.x, tap.y - center.y) <= maxDist) {
            GridPoint(col, row)
        } else {
            null
        }
    }

    companion object {
        fun fromCanvasSize(size: IntSize): FieldLayout =
            fromCanvasSize(Size(size.width.toFloat(), size.height.toFloat()))

        fun fromCanvasSize(size: Size): FieldLayout {
            val pad = size.minDimension * 0.04f
            val usableW = size.width - 2 * pad
            val usableH = size.height - 2 * pad
            return FieldLayout(
                pad = pad,
                stepX = usableW / (FieldSpec.WIDTH - 1),
                stepY = usableH / (FieldSpec.LENGTH - 1),
                width = size.width,
                height = size.height
            )
        }
    }
}
