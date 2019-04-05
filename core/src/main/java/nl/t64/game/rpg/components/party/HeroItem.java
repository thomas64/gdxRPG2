package nl.t64.game.rpg.components.party;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Setter
public class HeroItem {

    @Getter
    private String name;
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
        return level.getTotalXp();
    }

    int getLevel() {
        return level.getCurrent();
    }

    public Map<String, Integer> getAllHpStats() {
        return Map.of("lvlCur", level.getCurrent(),
                      "lvlHp", level.getHitpoints(),
                      "staCur", stamina.getCurrent(),
                      "staHp", stamina.getHitpoints(),
                      "eduCur", endurance.getCurrent(),
                      "eduHp", endurance.getHitpoints(),
                      "eduBon", endurance.getBonus());
    }

    public int getMaximumHp() {
        return level.getCurrent() + stamina.getCurrent() + endurance.getCurrent() + endurance.getBonus();
    }

    public int getCurrentHp() {
        return level.getHitpoints() + stamina.getHitpoints() + endurance.getHitpoints() + endurance.getBonus();
    }

}
