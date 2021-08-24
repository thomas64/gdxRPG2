package nl.t64.game.rpg.components.battle


class Battle(
    val battlers: List<Battler> = emptyList(),
    val isEscapable: Boolean = true,
    var hasWon: Boolean = false
)
