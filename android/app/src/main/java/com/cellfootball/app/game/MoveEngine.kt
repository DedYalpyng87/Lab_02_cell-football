package com.cellfootball.app.game

object MoveEngine {

    const val MOVE_SEGMENTS = 3

    fun canAddStep(state: MatchState, path: List<GridPoint>, next: GridPoint): Boolean {
        if (path.isEmpty()) return false
        val from = path.last()
        if (from == next) return false
        if (next !in from.neighbors()) return false
        if (hasSegment(state.lines, from, next)) return false

        val used = occupiedNodes(state.lines)
        if (next in used && next != path.first()) {
            if (!isOpponentGoalMouth(state, state.currentPlayer, next)) return false
        }
        if (next in path.dropLast(1)) return false

        return true
    }

    fun canStartStep(state: MatchState, next: GridPoint): Boolean =
        canAddStep(state, listOf(state.ball), next)

    fun completeMove(state: MatchState, path: List<GridPoint>): MatchState {
        require(path.size == MOVE_SEGMENTS + 1) {
            "path must have ${MOVE_SEGMENTS + 1} nodes"
        }
        val movingPlayer = state.currentPlayer
        val newSegments = (0 until MOVE_SEGMENTS).map { i ->
            PlayedSegment(path[i], path[i + 1], movingPlayer)
        }
        val newBall = path.last()
        val nextPlayer = movingPlayer.opponent()
        var updated = state.copy(
            ball = newBall,
            lines = state.lines + newSegments,
            moveInProgress = emptyList(),
            currentPlayer = nextPlayer
        )
        if (pathScoresGoal(path, movingPlayer)) {
            updated = applyGoal(updated, movingPlayer)
        }
        return updated
    }

    private fun applyGoal(state: MatchState, scorer: PlayerSide): MatchState =
        state.copy(
            scoreP1 = state.scoreP1 + if (scorer == PlayerSide.One) 1 else 0,
            scoreP2 = state.scoreP2 + if (scorer == PlayerSide.Two) 1 else 0,
            ball = GridPoint(FieldSpec.centerCol, FieldSpec.centerRow),
            moveInProgress = emptyList()
        )

    fun cancelMove(state: MatchState): MatchState =
        state.copy(moveInProgress = emptyList())

    fun allLegalPaths(state: MatchState): List<List<GridPoint>> {
        val results = mutableListOf<List<GridPoint>>()
        fun dfs(path: List<GridPoint>) {
            if (path.size == MOVE_SEGMENTS + 1) {
                results.add(path)
                return
            }
            for (next in path.last().neighbors()) {
                if (canAddStep(state, path, next)) {
                    dfs(path + next)
                }
            }
        }
        dfs(listOf(state.ball))
        return results
    }

    fun pickRandomPath(state: MatchState): List<GridPoint>? =
        allLegalPaths(state).randomOrNull()
}
