package nl.t64.game.rpg

import nl.t64.game.rpg.subjects.*


class BrokerManager {

    val actionObservers = ActionSubject()
    val blockObservers = BlockSubject()
    val bumpObservers = BumpSubject()
    val detectionObservers = DetectionSubject()
    val collisionObservers = CollisionSubject()

    val componentObservers = ComponentSubject()
    val lootObservers = LootSubject()
    val mapObservers = MapSubject()

    val partyObservers = PartySubject()
    val profileObservers = ProfileSubject()

}
