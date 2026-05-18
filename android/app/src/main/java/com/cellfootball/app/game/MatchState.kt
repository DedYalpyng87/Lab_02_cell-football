package com.cellfootball.app.game

enum class PlayerSide(val label: String) {
    One("Игрок 1"),
    Two("Игрок 2")
}

data class MatchState(
    val ball: GridPoint = GridPoint(FieldSpec.centerCol, FieldSpec.centerRow),
    val currentPlayer: PlayerSide = PlayerSide.One,
    val scoreP1: Int = 0,
    val scoreP2: Int = 0,
    val lines: List<PlayedSegment> = emptyList(),
    /** Узлы текущего незавершённого хода (первый — старт с мяча). */
    val moveInProgress: List<GridPoint> = emptyList()
) {
    val moveStep: Int
        get() = if (moveInProgress.isEmpty()) 0 else moveInProgress.size - 1
}

/** Отрезок между соседними узлами; цвет на поле — по игроку, который его провёл. */
data class PlayedSegment(
    val from: GridPoint,
    val to: GridPoint,
    val player: PlayerSide
)

fun newMatch(): MatchState = MatchState()
