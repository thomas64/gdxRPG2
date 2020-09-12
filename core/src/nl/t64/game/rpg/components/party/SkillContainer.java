package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import nl.t64.game.rpg.constants.SkillItemId;

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
        this.skills.put(SkillItemId.ALCHEMIST.name(), new Alchemist(alc));
        this.skills.put(SkillItemId.DIPLOMAT.name(), new Diplomat(dip));
        this.skills.put(SkillItemId.HEALER.name(), new Healer(hlr));
        this.skills.put(SkillItemId.LOREMASTER.name(), new Loremaster(lor));
        this.skills.put(SkillItemId.MECHANIC.name(), new Mechanic(mec));
        this.skills.put(SkillItemId.MERCHANT.name(), new Merchant(mer));
        this.skills.put(SkillItemId.RANGER.name(), new Ranger(ran));
        this.skills.put(SkillItemId.STEALTH.name(), new Stealth(stl));
        this.skills.put(SkillItemId.THIEF.name(), new Thief(thf));
        this.skills.put(SkillItemId.TROUBADOUR.name(), new Troubadour(trb));
        this.skills.put(SkillItemId.WARRIOR.name(), new Warrior(war));
        this.skills.put(SkillItemId.WIZARD.name(), new Wizard(wiz));
        this.skills.put(SkillItemId.HAFTED.name(), new Hafted(haf));
        this.skills.put(SkillItemId.MISSILE.name(), new Missile(mis));
        this.skills.put(SkillItemId.POLE.name(), new Pole(pol));
        this.skills.put(SkillItemId.SHIELD.name(), new Shield(shd));
        this.skills.put(SkillItemId.SWORD.name(), new Sword(swd));
        this.skills.put(SkillItemId.THROWN.name(), new Thrown(thr));
    }

    SkillItem getById(SkillItemId skillItemId) {
        return skills.get(skillItemId.name());
    }

    List<SkillItem> getAllAboveZero() {
        return Arrays.stream(SkillItemId.values())
                     .map(skillItemId -> skills.get(skillItemId.name()))
                     .filter(this::hasPositiveQuantity)
                     .collect(Collectors.toList());
    }

    private boolean hasPositiveQuantity(SkillItem skillItem) {
        return skillItem.rank > 0;
    }

}
