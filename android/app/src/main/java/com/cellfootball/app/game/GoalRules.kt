package com.cellfootball.app.game

/**
 * Ворота, которые защищает игрок.
 * Игрок 1 защищает низ экрана, атакует верх (row 0).
 * Игрок 2 / ИИ — наоборот.
 */
fun goalRowDefendedBy(defender: PlayerSide): Int = when (defender) {
    PlayerSide.One -> FieldSpec.LENGTH - 1
    PlayerSide.Two -> 0
}

/** Ворота, в которые атакующий может забить. */
fun goalRowForAttacker(attacker: PlayerSide): Int =
    goalRowDefendedBy(attacker.opponent())

fun PlayerSide.opponent(): PlayerSide = when (this) {
    PlayerSide.One -> PlayerSide.Two
    PlayerSide.Two -> PlayerSide.One
}

fun isOnGoalMouth(point: GridPoint): Boolean =
    point.col in FieldSpec.goalStartCol..FieldSpec.goalEndCol &&
        (point.row == 0 || point.row == FieldSpec.LENGTH - 1)

/** Сегмент пересекает линию ворот «внутрь» (по направлению к воротам). */
fun segmentCrossesGoalLine(from: GridPoint, to: GridPoint, goalRow: Int): Boolean {
    if (to.col !in FieldSpec.goalStartCol..FieldSpec.goalEndCol) return false
    return when (goalRow) {
        0 -> from.row > 0 && to.row == 0
        FieldSpec.LENGTH - 1 -> from.row < FieldSpec.LENGTH - 1 && to.row == FieldSpec.LENGTH - 1
        else -> false
    }
}

fun pathScoresGoal(path: List<GridPoint>, scorer: PlayerSide): Boolean {
    val goalRow = goalRowForAttacker(scorer)
    for (i in 0 until path.size - 1) {
        if (segmentCrossesGoalLine(path[i], path[i + 1], goalRow)) return true
    }
    return false
}

/** Разрешить шаг на линию ворот соперника (для забития). */
fun isOpponentGoalMouth(state: MatchState, attacker: PlayerSide, point: GridPoint): Boolean {
    val goalRow = goalRowForAttacker(attacker)
    return point.row == goalRow && point.col in FieldSpec.goalStartCol..FieldSpec.goalEndCol
}
