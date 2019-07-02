package nl.t64.game.rpg.components.party;

import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Setter
class SkillContainer {

    private Stealth stealth;
    private Hafted hafted;
    private Pole pole;
    private Shield shield;
    private Sword sword;

    List<SkillType> getAllAboveZero() {
        return Arrays.stream(SkillType.values())
                     .filter(this::hasPositiveQuantity)
                     .collect(Collectors.toList());
    }

    int getOwnSkillOf(SkillType skillType) {
        switch (skillType) {
            case STEALTH:
                return stealth.actual;
            case HAFTED:
                return hafted.actual;
            case POLE:
                return pole.actual;
            case SHIELD:
                return shield.actual;
            case SWORD:
                return sword.actual;
            default:
                return 0; // todo, deze vervangen door onderstaande regel uiteindelijk, wanneer ze allemaal in deze lijst staan.
//                throw new IllegalArgumentException(String.format("SkillType '%s' not usable.", skillType));
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

    private boolean hasPositiveQuantity(SkillType skillType) {
        return getOwnSkillOf(skillType) > 0;
    }

}
