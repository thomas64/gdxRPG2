package nl.t64.game.rpg.components.battle


class Battle(
    val battlers: List<Battler> = emptyList(),
    var hasWon: Boolean = false
)
