package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


class SkillContainer {

    private static final int NUMBER_OF_SKILL_SLOTS = 18;

    private final Map<String, SkillItem> skills;

    private SkillContainer() {
        this.skills = new HashMap<>(NUMBER_OF_SKILL_SLOTS);
    }

    @JsonCreator
    @ConstructorProperties({
            "alchemist", "diplomat", "healer", "loremaster", "mechanic", "merchant",
            "ranger", "stealth", "thief", "troubadour", "warrior", "wizard",
            "hafted", "missile", "pole", "shield", "sword", "thrown"})
    private SkillContainer(int alc, int dip, int hlr, int lor, int mec, int mer,
                           int ran, int stl, int thf, int trb, int war, int wiz,
                           int haf, int mis, int pol, int shd, int swd, int thr) {
        this();
        this.skills.put(SkillType.ALCHEMIST.name(), new Alchemist(alc));
        this.skills.put(SkillType.DIPLOMAT.name(), new Diplomat(dip));
        this.skills.put(SkillType.HEALER.name(), new Healer(hlr));
        this.skills.put(SkillType.LOREMASTER.name(), new Loremaster(lor));
        this.skills.put(SkillType.MECHANIC.name(), new Mechanic(mec));
        this.skills.put(SkillType.MERCHANT.name(), new Merchant(mer));
        this.skills.put(SkillType.RANGER.name(), new Ranger(ran));
        this.skills.put(SkillType.STEALTH.name(), new Stealth(stl));
        this.skills.put(SkillType.THIEF.name(), new Thief(thf));
        this.skills.put(SkillType.TROUBADOUR.name(), new Troubadour(trb));
        this.skills.put(SkillType.WARRIOR.name(), new Warrior(war));
        this.skills.put(SkillType.WIZARD.name(), new Wizard(wiz));
        this.skills.put(SkillType.HAFTED.name(), new Hafted(haf));
        this.skills.put(SkillType.MISSILE.name(), new Missile(mis));
        this.skills.put(SkillType.POLE.name(), new Pole(pol));
        this.skills.put(SkillType.SHIELD.name(), new Shield(shd));
        this.skills.put(SkillType.SWORD.name(), new Sword(swd));
        this.skills.put(SkillType.THROWN.name(), new Thrown(thr));
    }

    List<SkillType> getAllAboveZero() {
        return Arrays.stream(SkillType.values())
                     .filter(this::hasPositiveQuantity)
                     .collect(Collectors.toList());
    }

    int getRankOf(SkillType skillType) {
        return skills.get(skillType.name()).rank;
    }

    int getBonusSkillOf(SkillType skillType) {
        return skills.get(skillType.name()).bonus;
    }

    private boolean hasPositiveQuantity(SkillType skillType) {
        return getRankOf(skillType) > 0;
    }

}
