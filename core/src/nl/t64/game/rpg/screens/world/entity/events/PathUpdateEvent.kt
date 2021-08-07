package nl.t64.game.rpg.screens.world.entity.events

import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode


class PathUpdateEvent(val path: DefaultGraphPath<TiledNode>) : Event
