package nl.t64.game.rpg.components.party;

import lombok.Setter;

import java.util.Map;


@Setter
public class HeroItem {

    String name;
    private Level level;
    private Endurance endurance;
    private Stamina stamina;

    public int getNeededXpForNextLevel() {
        return level.getNeededXpForNextLevel();
    }

    public int getXpDeltaBetweenLevels() {
        return level.getXpDeltaBetweenLevels();
    }

    int getTotalXp() {
        return level.totalXp;
    }

    int getLevel() {
        return level.current;
    }

    public Map<String, Integer> getAllHpStats() {
        return Map.of("lvlCur", level.current,
                      "lvlHp", level.hitpoints,
                      "staCur", stamina.current,
                      "staHp", stamina.hitpoints,
                      "eduCur", endurance.current,
                      "eduHp", endurance.hitpoints,
                      "eduBon", endurance.bonus);
    }

    public int getMaximumHp() {
        return level.current + stamina.current + endurance.current + endurance.bonus;
    }

    public int getCurrentHp() {
        return level.hitpoints + stamina.hitpoints + endurance.hitpoints + endurance.bonus;
    }

}
