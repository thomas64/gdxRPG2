package nl.t64.game.rpg.components.loot;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;


@Getter
public class Loot {

    private Map<String, Integer> content;
    private int trapLevel;
    private int lockLevel;
    private int xp;

    Loot() {
        this.content = Collections.emptyMap();
        this.trapLevel = 0;
        this.lockLevel = 0;
        this.xp = 0;
    }

    public boolean isTaken() {
        return content.isEmpty();
    }

    public void clearContent() {
        content = Collections.emptyMap();
    }

    public void updateContent(Map<String, Integer> newContent) {
        content = newContent;
    }

    public boolean isTrapped() {
        return trapLevel > 0;
    }

    public boolean canDisarmTrap(int mechanicLevel) {
        return mechanicLevel >= trapLevel;
    }

    public void disarmTrap() {
        trapLevel = 0;
    }

    public boolean isLocked() {
        return lockLevel > 0;
    }

    public boolean canPickLock(int thiefLevel) {
        return thiefLevel >= lockLevel;
    }

    public void pickLock() {
        lockLevel = 0;
    }

    public void clearXp() {
        xp = 0;
    }

}
