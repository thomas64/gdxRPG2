package nl.t64.game.rpg.components.party;

import lombok.Setter;


@Setter
class SkillContainer {

    private Stealth stealth;
    private Hafted hafted;
    private Pole pole;
    private Shield shield;
    private Sword sword;

    int getOwnSkillOf(SkillType skillType) {
        switch (skillType) {
            case STEALTH:
                return stealth.current;
            case HAFTED:
                return hafted.current;
            case POLE:
                return pole.current;
            case SHIELD:
                return shield.current;
            case SWORD:
                return sword.current;
            default:
                throw new IllegalArgumentException(String.format("SkillType '%s' not usable.", skillType));
        }
    }

    int getBonusSkillOf(SkillType skillType) {
        switch (skillType) {
            case STEALTH:
                return stealth.bonus;
            default:
                return 0;
        }
    }

}
