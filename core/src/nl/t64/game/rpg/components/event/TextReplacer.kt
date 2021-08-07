package nl.t64.game.rpg.components.event

import nl.t64.game.rpg.Utils


object TextReplacer {

    fun replace(str: String): String {
        var substr = str.substring(str.indexOf("%"))
        substr = substr.substring(0, substr.indexOf("%", 1) + 1)
        val hasGamePad = Utils.isGamepadConnected()
        return when (substr) {
            "%action%" -> str.replace(substr, if (hasGamePad) "'A' button" else "'A' key")
            "%inventory%" -> str.replace(substr, if (hasGamePad) "'Y' button" else "'I' key")
            "%slow%" -> str.replace(substr, if (hasGamePad) "'L1' button" else "'Ctrl' key")
            else -> throw IllegalArgumentException("Unexpected value: '$substr'")
        }
    }

}