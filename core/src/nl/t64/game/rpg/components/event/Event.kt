package nl.t64.game.rpg.components.event

import com.badlogic.gdx.utils.GdxRuntimeException
import com.fasterxml.jackson.annotation.JsonProperty
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.components.condition.ConditionDatabase


class Event(
    private val type: String = "",
    @JsonProperty(value = "condition")
    val conditionIds: List<String> = emptyList(),
    val conversationId: String? = null,
    val entityId: String? = null,
    private val text: String? = null
) {
    var hasPlayed: Boolean = false

    fun possibleStart() {
        if (!hasPlayed && isMeetingCondition()) {
            hasPlayed = true
            start()
        }
    }

    private fun isMeetingCondition(): Boolean {
        return ConditionDatabase.isMeetingConditions(conditionIds)
    }

    private fun start() {
        when (type) {
            "conversation" -> brokerManager.componentObservers.notifyShowConversationDialog(conversationId!!, entityId!!)
            "messagebox" -> brokerManager.componentObservers.notifyShowMessageDialog(replaceText(text!!))
            else -> throw GdxRuntimeException("Event must contain 'type'.")
        }
    }

    private fun replaceText(str: String): String {
        var substr = str.substring(str.indexOf("%"))
        substr = substr.substring(0, substr.indexOf("%", 1) + 1)
        val hasGamePad = Utils.isGamepadConnected()
        return when (substr) {
            "%action%" -> str.replace(substr, if (hasGamePad) "'A' button" else "'A' key")
            "%inventory%" -> str.replace(substr, if (hasGamePad) "'Y' button" else "'I' key")
            "%slow%" -> str.replace(substr, if (hasGamePad) "'L1' button" else "'Ctrl' key")
            else -> throw IllegalStateException("Unexpected value: '$substr'")
        }
    }

}
