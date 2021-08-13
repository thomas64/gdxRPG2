package nl.t64.game.rpg.components.event

import nl.t64.game.rpg.Utils


object TextReplacer {

    fun replace(listOfStrings: List<String>): String {
        return listOfStrings.joinToString(System.lineSeparator()) { replace(it) }
    }

    private fun replace(str: String): String {
        val firstIndex = str.indexOf("%").takeUnless { it == -1 } ?: return str
        val substr = str.substring(firstIndex).let {
            it.substring(0, it.indexOf("%", 1) + 1)
        }
        val hasGamePad = Utils.isGamepadConnected()
        return when (substr) {
            "%action%" -> str.replace(substr, if (hasGamePad) "'A' button" else "'A' key")
            "%inventory%" -> str.replace(substr, if (hasGamePad) "'Y' button" else "'I' key")
            "%slow%" -> str.replace(substr, if (hasGamePad) "'L1' button" else "'Ctrl' key")
            else -> throw IllegalArgumentException("Unexpected value: '$substr'")
        }
    }

}
