package com.cellfootball.app.game

/**
 * Размеры поля по [game-rules.md] §2–3: 20×28 узлов, ворота на короткой стороне.
 */
object FieldSpec {
    const val WIDTH = 20
    const val LENGTH = 28
    const val GOAL_WIDTH = 6

    val centerCol: Int = (WIDTH - 1) / 2
    val centerRow: Int = (LENGTH - 1) / 2
    val goalStartCol: Int = (WIDTH - GOAL_WIDTH) / 2
    val goalEndCol: Int = goalStartCol + GOAL_WIDTH - 1
}

data class GridPoint(val col: Int, val row: Int) {
    init {
        require(col in 0 until FieldSpec.WIDTH) { "col out of range: $col" }
        require(row in 0 until FieldSpec.LENGTH) { "row out of range: $row" }
    }
}
