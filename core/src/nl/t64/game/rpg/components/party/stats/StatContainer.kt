package nl.t64.game.rpg.components.party.stats

import java.beans.ConstructorProperties


private const val NUMBER_OF_STAT_SLOTS = 7

class StatContainer() {

    private lateinit var level: Level
    private val stats: MutableMap<String, StatItem> = HashMap(NUMBER_OF_STAT_SLOTS)

    @ConstructorProperties(
        "level", "intelligence", "willpower", "dexterity", "agility", "endurance", "strength", "stamina")
    constructor(lvl: Int, inl: Int, wil: Int, dex: Int, agi: Int, edu: Int, str: Int, sta: Int) : this() {
        this.level = Level(lvl)
        this.stats[StatItemId.INTELLIGENCE.name] = Intelligence(inl)
        this.stats[StatItemId.WILLPOWER.name] = Willpower(wil)
        this.stats[StatItemId.DEXTERITY.name] = Dexterity(dex)
        this.stats[StatItemId.AGILITY.name] = Agility(agi)
        this.stats[StatItemId.ENDURANCE.name] = Endurance(edu)
        this.stats[StatItemId.STRENGTH.name] = Strength(str)
        this.stats[StatItemId.STAMINA.name] = Stamina(sta)
    }

    fun getXpNeededForNextLevel(): Int = level.getXpNeededForNextLevel()
    fun getXpDeltaBetweenLevels(): Int = level.getXpDeltaBetweenLevels()
    fun getTotalXp(): Int = level.totalXp
    fun getXpToInvest(): Int = level.xpToInvest
    fun getLevel(): Int = level.rank

    fun gainXp(amount: Int, hasGainedLevel: (Boolean) -> Unit) {
        level.gainXp(amount, hasGainedLevel)
    }

    fun getAllHpStats(): Map<String, Int> {
        return mapOf(Pair("lvlRank", level.rank),
                     Pair("lvlVari", level.variable),
                     Pair("staRank", stats[StatItemId.STAMINA.name]!!.rank),
                     Pair("staVari", stats[StatItemId.STAMINA.name]!!.variable),
                     Pair("eduRank", stats[StatItemId.ENDURANCE.name]!!.rank),
                     Pair("eduVari", stats[StatItemId.ENDURANCE.name]!!.variable),
                     Pair("eduBon", stats[StatItemId.ENDURANCE.name]!!.bonus))
    }

    fun getMaximumHp(): Int {
        return (level.rank
                + stats[StatItemId.STAMINA.name]!!.rank
                + stats[StatItemId.ENDURANCE.name]!!.rank
                + stats[StatItemId.ENDURANCE.name]!!.bonus)
    }

    fun getCurrentHp(): Int {
        return (level.variable
                + stats[StatItemId.STAMINA.name]!!.variable
                + stats[StatItemId.ENDURANCE.name]!!.variable
                + stats[StatItemId.ENDURANCE.name]!!.bonus)
    }

    fun getById(statItemId: StatItemId): StatItem {
        return stats[statItemId.name]!!
    }

    fun getAll(): List<StatItem> {
        return StatItemId.values().map { stats[it.name]!! }
    }

    fun takeDamage(damage: Int) {
        level.takeDamage(damage)?.let { it1 ->
            (stats[StatItemId.STAMINA.name] as Stamina).takeDamage(it1)?.let { it2 ->
                (stats[StatItemId.ENDURANCE.name] as Endurance).takeDamage(it2)
            }
        }
    }

    fun getInflictDamageStaminaPenalty(): Int {
        return (stats[StatItemId.STAMINA.name] as Stamina).getInflictDamagePenalty()
    }

}
