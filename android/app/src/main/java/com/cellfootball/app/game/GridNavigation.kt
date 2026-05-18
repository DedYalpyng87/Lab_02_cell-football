package com.cellfootball.app.game

private val DIRECTIONS = listOf(
    -1 to 0, 1 to 0, 0 to -1, 0 to 1,
    -1 to -1, -1 to 1, 1 to -1, 1 to 1
)

fun GridPoint.neighbors(): List<GridPoint> =
    DIRECTIONS.mapNotNull { (dc, dr) ->
        val nc = col + dc
        val nr = row + dr
        if (nc in 0 until FieldSpec.WIDTH && nr in 0 until FieldSpec.LENGTH) {
            GridPoint(nc, nr)
        } else {
            null
        }
    }

fun PlayedSegment.normalized(): Pair<GridPoint, GridPoint> =
    if (from.col < to.col || (from.col == to.col && from.row <= to.row)) {
        from to to
    } else {
        to to from
    }

fun segmentSet(lines: List<PlayedSegment>): Set<Pair<GridPoint, GridPoint>> =
    lines.map { it.normalized() }.toSet()

fun hasSegment(lines: List<PlayedSegment>, a: GridPoint, b: GridPoint): Boolean {
    val key = if (a.col < b.col || (a.col == b.col && a.row <= b.row)) a to b else b to a
    return segmentSet(lines).contains(key)
}

fun occupiedNodes(lines: List<PlayedSegment>): Set<GridPoint> =
    lines.flatMap { listOf(it.from, it.to) }.toSet()
