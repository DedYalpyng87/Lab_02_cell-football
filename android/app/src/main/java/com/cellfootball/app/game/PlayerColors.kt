package com.cellfootball.app.game

import androidx.compose.ui.graphics.Color

object PlayerColors {
    val playerOneLine = Color(0xFF42A5F5)
    val playerTwoLine = Color(0xFFFF9800)

    fun lineColor(player: PlayerSide): Color = when (player) {
        PlayerSide.One -> playerOneLine
        PlayerSide.Two -> playerTwoLine
    }
}
