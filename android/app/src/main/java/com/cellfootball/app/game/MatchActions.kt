package com.cellfootball.app.game

fun MatchState.pathAnchor(): List<GridPoint> =
    if (moveInProgress.isEmpty()) listOf(ball) else moveInProgress

fun MatchState.canAppendTo(node: GridPoint): Boolean {
    if (moveInProgress.size >= MoveEngine.MOVE_SEGMENTS + 1) return false
    return MoveEngine.canAddStep(this, pathAnchor(), node)
}

/** Добавить шаг (тап или отпускание пальца на узле). Ход не завершается автоматически. */
fun MatchState.tryAppendStep(node: GridPoint): MatchState {
    if (!canAppendTo(node)) return this
    val newPath = if (moveInProgress.isEmpty()) {
        listOf(ball, node)
    } else {
        moveInProgress + node
    }
    return copy(moveInProgress = newPath)
}

fun MatchState.canConfirmMove(): Boolean =
    moveInProgress.size == MoveEngine.MOVE_SEGMENTS + 1

fun MatchState.confirmMove(): MatchState {
    if (!canConfirmMove()) return this
    return MoveEngine.completeMove(this, moveInProgress)
}

fun MatchState.afterHumanMove(mode: GameMode?): MatchState {
    if (mode != GameMode.VsAi || currentPlayer != PlayerSide.Two) return this
    return applyAiTurn()
}

private fun MatchState.applyAiTurn(): MatchState {
    val path = MoveEngine.pickRandomPath(this) ?: return this
    return MoveEngine.completeMove(this, path)
}
