package nl.t64.game.rpg.components.event

import com.fasterxml.jackson.annotation.JsonProperty
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.components.condition.ConditionDatabase


class Event(
    private val type: String = "",
    @JsonProperty(value = "condition")
    val conditionIds: List<String> = emptyList(),
    val conversationId: String? = null,
    val entityId: String? = null,
    val text: String? = null
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
            "messagebox" -> brokerManager.componentObservers.notifyShowMessageDialog(TextReplacer.replace(text!!))
            else -> throw IllegalStateException("Event does not recognize type: '$type'.")
        }
    }

}
