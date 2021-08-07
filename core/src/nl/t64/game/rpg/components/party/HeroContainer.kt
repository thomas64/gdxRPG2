package nl.t64.game.rpg.components.party

import com.badlogic.gdx.Gdx
import nl.t64.game.rpg.Utils.readValue


private const val HERO_CONFIGS = "configs/characters/hero1.json"

class HeroContainer {

    private val heroes: MutableMap<String, HeroItem> = readValue<HeroItem>(
        Gdx.files.internal(HERO_CONFIGS).readString()).onEach { it.value.id = it.key }
    val size: Int get() = heroes.size

    fun addHero(hero: HeroItem) {
        heroes[hero.id] = hero
    }

    fun removeHero(heroId: String) {
        heroes.remove(heroId)
    }

    fun getCertainHero(heroId: String): HeroItem {
        return heroes[heroId]!!
    }

    fun contains(heroId: String): Boolean {
        return heroes.containsKey(heroId)
    }

}
