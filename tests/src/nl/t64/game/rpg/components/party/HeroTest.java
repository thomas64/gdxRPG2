package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.constants.Constant;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class HeroTest extends GameTest {

    private HeroContainer heroes;
    private PartyContainer party;

    @BeforeEach
    private void setup() {
        heroes = new HeroContainer();
        party = new PartyContainer();
        addHeroToParty(Constant.PLAYER_ID);
    }

    private void addHeroToParty(String heroId) {
        HeroItem hero = heroes.getHero(heroId);
        heroes.removeHero(heroId);
        party.addHero(hero);
    }

    private void removeHeroFromParty(String heroId) {
        HeroItem hero = party.getHero(heroId);
        party.removeHero(heroId);
        heroes.addHero(hero);
    }

    @Test
    void whenDataIsCreated_ShouldContainPlayer() {
        final HeroItem mozes = party.getHero("mozes");
        assertThat(heroes.contains(Constant.PLAYER_ID)).isFalse();
        assertThat(heroes.getSize()).isEqualTo(13);
        assertThat(party.contains(Constant.PLAYER_ID)).isTrue();
        assertThat(party.getSize()).isEqualTo(1);
        assertThat(party.containsExactlyEqualTo(null)).isFalse();
        assertThat(party.containsExactlyEqualTo(mozes)).isTrue();
    }

    @Test
    void whenPlayerIsRemovedFromParty_ShouldThrowException() {
        assertThat(party.contains(Constant.PLAYER_ID)).isTrue();
        assertThat(party.getSize()).isEqualTo(1);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> removeHeroFromParty(Constant.PLAYER_ID))
                .withMessage("Cannot remove player from party.");
    }

    @Test
    void whenHeroIsAddedToParty_ShouldBeLastInParty() {
        final HeroItem mozes = party.getHero("mozes");
        assertThat(party.isHeroLast(mozes)).isTrue();

        final String luana = "luana";
        addHeroToParty(luana);
        assertThat(party.isHeroLast(mozes)).isFalse();
        assertThat(party.isHeroLast(party.getHero(luana))).isTrue();
    }

    @Test
    void whenHeroIsAddedToParty_ShouldSetRecruitedTrue() {
        final String luana = "luana";
        assertThat(heroes.getHero(luana).isHasBeenRecruited()).isFalse();
        addHeroToParty(luana);
        assertThat(party.getHero(luana).isHasBeenRecruited()).isTrue();
        removeHeroFromParty(luana);
        assertThat(heroes.getHero(luana).isHasBeenRecruited()).isTrue();
    }

    @Test
    void whenHeroIsAddedToParty_ShouldBeRemovedFromHeroContainer() {
        final String luana = "luana";

        assertThat(heroes.getSize()).isEqualTo(13);
        assertThat(heroes.contains(luana)).isTrue();
        assertThat(party.getSize()).isEqualTo(1);
        assertThat(party.contains(luana)).isFalse();

        addHeroToParty(luana);

        assertThat(heroes.getSize()).isEqualTo(12);
        assertThat(heroes.contains(luana)).isFalse();
        assertThat(party.getSize()).isEqualTo(2);
        assertThat(party.contains(luana)).isTrue();
    }

    @Test
    void whenHeroIsRemovedFromParty_ShouldBeAddedToHeroContainer() {
        final String luana = "luana";

        addHeroToParty(luana);

        assertThat(heroes.getSize()).isEqualTo(12);
        assertThat(heroes.contains(luana)).isFalse();
        assertThat(party.getSize()).isEqualTo(2);
        assertThat(party.contains(luana)).isTrue();

        removeHeroFromParty(luana);

        assertThat(heroes.getSize()).isEqualTo(13);
        assertThat(heroes.contains(luana)).isTrue();
        assertThat(party.getSize()).isEqualTo(1);
        assertThat(party.contains(luana)).isFalse();
    }

    @Test
    void whenPartyIsFull_ShouldThrowException() {
        addHeroToParty("luana");
        addHeroToParty("reignald");
        addHeroToParty("ryiah");
        addHeroToParty("valter");
        addHeroToParty("galen");

        assertThat(heroes.getSize()).isEqualTo(8);
        assertThat(party.getSize()).isEqualTo(6);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> addHeroToParty("jaspar"))
                .withMessage("Party is full.");
    }

    @Test
    void whenImpossibleItemIsForceSet_ShouldOverwriteExistingItem() {
        final HeroItem mozes = party.getHero("mozes");
        assertThat(mozes.getInventoryItem(InventoryGroup.WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_shortsword");

        final InventoryItem newWeapon = InventoryDatabase.getInstance().createInventoryItem("masterwork_lance");
        mozes.forceSetInventoryItemFor(InventoryGroup.WEAPON, newWeapon);
        assertThat(mozes.getInventoryItem(InventoryGroup.WEAPON)).containsSame(newWeapon);
        mozes.clearInventoryItemFor(InventoryGroup.WEAPON);
        assertThat(mozes.getInventoryItem(InventoryGroup.WEAPON)).isEmpty();
    }

    @Test
    void whenImpossibleItemIsChecked_ShouldGetMessage() {
        final HeroItem mozes = party.getHero("mozes");
        final HeroItem ryiah = heroes.getHero("ryiah");
        final InventoryItem legendaryStaff = InventoryDatabase.getInstance().createInventoryItem("legendary_staff");
        final InventoryItem masterworkLance = InventoryDatabase.getInstance().createInventoryItem("masterwork_lance");
        final InventoryItem basicDagger = InventoryDatabase.getInstance().createInventoryItem("basic_dagger");
        final InventoryItem chest = InventoryDatabase.getInstance().createInventoryItem("basic_light_chest");
        final InventoryItem bow = InventoryDatabase.getInstance().createInventoryItem("basic_shortbow");
        final InventoryItem shield = InventoryDatabase.getInstance().createInventoryItem("basic_light_shield");

        Optional<String> message;

        message = mozes.createMessageIfNotAbleToEquip(legendaryStaff);
        assertThat(message).contains("""
                                             Mozes needs the Pole skill
                                             to equip that Legendary Staff.""");

        message = ryiah.createMessageIfNotAbleToEquip(legendaryStaff);
        assertThat(message).contains("""
                                             Ryiah needs 30 Intelligence
                                             to equip that Legendary Staff.""");

        message = ryiah.createMessageIfNotAbleToEquip(masterworkLance);
        assertThat(message).contains("""
                                             Ryiah needs 20 Strength
                                             to equip that Masterwork Lance.""");

        message = mozes.createMessageIfNotAbleToEquip(basicDagger);
        assertThat(message).isEmpty();

        message = mozes.createMessageIfNotAbleToEquip(chest);
        assertThat(message).isEmpty();

        message = mozes.createMessageIfNotAbleToEquip(bow);
        assertThat(message).contains("""
                                             Cannot equip the Basic Shortbow.
                                             First unequip the Basic Light Shield.""");

        mozes.clearInventoryItemFor(InventoryGroup.SHIELD);
        mozes.forceSetInventoryItemFor(InventoryGroup.WEAPON, bow);
        message = mozes.createMessageIfNotAbleToEquip(shield);
        assertThat(message).contains("""
                                             Cannot equip the Basic Light Shield.
                                             First unequip the Basic Shortbow.""");
    }

    @Test
    void whenHeroesAreCreated_ShouldHaveRightStats() {
        final HeroItem mozes = party.getHero("mozes");
        final HeroItem luana = heroes.getHero("luana");
        final HeroItem valter = heroes.getHero("valter");
        final HeroItem luthais = heroes.getHero("luthais");
        final HeroItem iellwen = heroes.getHero("iellwen");

        assertThat(party.getHero(0)).isEqualTo(mozes);

        assertThat(mozes.getSkillValueOf(InventoryGroup.SHIELD, SkillItemId.STEALTH)).isEqualTo(-5);
        assertThat(mozes.getStatValueOf(InventoryGroup.SHIELD, StatItemId.AGILITY)).isEqualTo(0);
        assertThat(mozes.getId()).isEqualTo("mozes");
        assertThat(mozes.getName()).isEqualTo("Mozes");
        assertThat(mozes.getSchool()).isEqualTo(SchoolType.UNKNOWN);
        assertThat(mozes.hasSameIdAs(mozes)).isTrue();
        assertThat(mozes.isPlayer()).isTrue();
        assertThat(mozes.getXpDeltaBetweenLevels()).isEqualTo(20);
        assertThat(mozes.getAllStats()).extracting("id")
                                       .containsExactly(StatItemId.INTELLIGENCE,
                                                        StatItemId.WILLPOWER,
                                                        StatItemId.STRENGTH,
                                                        StatItemId.DEXTERITY,
                                                        StatItemId.AGILITY,
                                                        StatItemId.ENDURANCE,
                                                        StatItemId.STAMINA);
        assertThat(mozes.getStatById(StatItemId.INTELLIGENCE).getXpCostForNextLevel()).isEqualTo(43);
        assertThat(mozes.getSkillById(SkillItemId.STEALTH).getXpCostForNextLevel(0)).isEqualTo(16);
        assertThat(mozes.getSkillById(SkillItemId.STEALTH).getGoldCostForNextLevel()).isEqualTo(8);

        assertThat(mozes.getExtraStatForVisualOf(mozes.getStatById(StatItemId.AGILITY))).isEqualTo(-1);
        assertThat(iellwen.getExtraSkillForVisualOf(iellwen.getSkillById(SkillItemId.STEALTH))).isEqualTo(-1);

        assertThat(mozes.getInventoryItem(InventoryGroup.WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_shortsword");
        assertThat(mozes.getInventoryItem(InventoryGroup.SHIELD)).get().hasFieldOrPropertyWithValue("id", "basic_light_shield");
        assertThat(mozes.getInventoryItem(InventoryGroup.HELMET)).isEmpty();
        assertThat(mozes.getInventoryItem(InventoryGroup.CHEST)).get().hasFieldOrPropertyWithValue("id", "basic_medium_chest");

        assertThat(luana.getInventoryItem(InventoryGroup.WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_dart");
        assertThat(luana.getInventoryItem(InventoryGroup.SHIELD)).isEmpty();
        assertThat(luana.getInventoryItem(InventoryGroup.HELMET)).isEmpty();
        assertThat(luana.getInventoryItem(InventoryGroup.CHEST)).get().hasFieldOrPropertyWithValue("id", "basic_light_chest");

        assertThat(luana.getAllSkillsAboveZero()).extracting("id")
                                                 .containsExactly(SkillItemId.MECHANIC,
                                                                  SkillItemId.STEALTH,
                                                                  SkillItemId.THIEF,
                                                                  SkillItemId.SWORD,
                                                                  SkillItemId.THROWN);

        assertThat(valter.getAllSpells()).extracting("id")
                                         .containsExactly("dragon_flames");

        assertThat(luthais.getAllSpells()).extracting("id", "rank")
                                          .contains(Tuple.tuple("fireball", 8));
        final int loremaster = luthais.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER);
        assertThat(luthais.getSkillById(SkillItemId.WIZARD).getXpCostForNextLevel(loremaster)).isZero();

        assertThat(StatItemId.INTELLIGENCE.getTitle()).isEqualTo("Intelligence");
        assertThat(SkillItemId.ALCHEMIST.getTitle()).isEqualTo("Alchemist");
        assertThat(CalcAttributeId.WEIGHT.getTitle()).isEqualTo("Weight");
        assertThat(SchoolType.NONE.getTitle()).isEqualTo("No");
        assertThat(ResourceType.GOLD.getTitle()).isEqualTo("Gold");
        assertThat(InventoryGroup.WEAPON.getTitle()).isEqualTo("Weapon");
        assertThat(InventoryMinimal.SKILL.getTitle()).isEqualTo("Skill");
    }

    @Test
    void whenHeroIsCreated_ShouldHaveRightHpStats() {
        final HeroItem mozes = party.getHero("mozes");
        final var actual = mozes.getAllHpStats();
        final var expected = Map.of("lvlRank", 1,
                                    "lvlVari", 1,
                                    "staRank", 30,
                                    "staVari", 30,
                                    "eduRank", 15,
                                    "eduVari", 15,
                                    "eduBon", 0);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenItemMakesStatLowerThanZero_ShouldReturnOne() {
        final HeroItem jaspar = heroes.getHero("jaspar");
        final InventoryItem itemMock = Mockito.mock(InventoryItem.class);
        Mockito.when(itemMock.getAttributeOfStatItemId(StatItemId.DEXTERITY)).thenReturn(-200);
        jaspar.forceSetInventoryItemFor(InventoryGroup.SHIELD, itemMock);

        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(1);
        assertThat(jaspar.getExtraStatForVisualOf(jaspar.getStatById(StatItemId.DEXTERITY))).isEqualTo(-14);
    }

    @Test
    void whenItemMakesSkillLowerThanZero_ShouldReturnZero() {
        final HeroItem faeron = heroes.getHero("faeron");
        final InventoryItem itemMock = Mockito.mock(InventoryItem.class);
        Mockito.when(itemMock.getAttributeOfSkillItemId(SkillItemId.STEALTH)).thenReturn(-200);
        faeron.forceSetInventoryItemFor(InventoryGroup.SHIELD, itemMock);

        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isZero();
        assertThat(faeron.getExtraSkillForVisualOf(faeron.getSkillById(SkillItemId.STEALTH))).isEqualTo(-10);
    }

    @Test
    void whenGetPreviousOrNextHeroFromParty_ShouldReturnThePreviousOrNextHero() {
        addHeroToParty("luana");
        addHeroToParty("reignald");
        addHeroToParty("ryiah");
        addHeroToParty("valter");
        addHeroToParty("galen");
        assertThat(party.getPreviousHero(party.getHero("mozes"))).isEqualTo(party.getHero("galen"));
        assertThat(party.getNextHero(party.getHero("mozes"))).isEqualTo(party.getHero("luana"));
        assertThat(party.getPreviousHero(party.getHero("ryiah"))).isEqualTo(party.getHero("reignald"));
        assertThat(party.getNextHero(party.getHero("ryiah"))).isEqualTo(party.getHero("valter"));
        assertThat(party.getPreviousHero(party.getHero("galen"))).isEqualTo(party.getHero("valter"));
        assertThat(party.getNextHero(party.getHero("galen"))).isEqualTo(party.getHero("mozes"));
    }

    @Test
    void whenHeroesAreAdded_ShouldIncreaseTheSumOfSkill() {
        assertThat(party.getSumOfSkill(SkillItemId.MERCHANT)).isZero();
        addHeroToParty("kiara");
        assertThat(party.getSumOfSkill(SkillItemId.MERCHANT)).isEqualTo(4);
        addHeroToParty("duilio");
        assertThat(party.getSumOfSkill(SkillItemId.MERCHANT)).isEqualTo(9);
    }

    @Test
    void whenAskedForHighestSkill_ShouldReturnThatBestHero() {
        addHeroToParty("luana");
        addHeroToParty("reignald");
        addHeroToParty("ryiah");
        addHeroToParty("valter");
        assertThat(party.getHeroWithHighestSkill(SkillItemId.THIEF)).isEqualTo(party.getHero("luana"));
        assertThat(party.getHeroWithHighestSkill(SkillItemId.MECHANIC)).isEqualTo(party.getHero("valter"));
        // no ranger present
        assertThat(party.getHeroWithHighestSkill(SkillItemId.RANGER)).isEqualTo(party.getHero("mozes"));
        // two with same highest
        assertThat(party.getHeroWithHighestSkill(SkillItemId.THROWN)).isEqualTo(party.getHero("luana"));
    }

    @Test
    void whenHeroGainsXp_ShouldGainXp() {
        final HeroItem mozes = party.getHero("mozes");
        final StringBuilder sb = new StringBuilder();
        assertThat(mozes.getTotalXp()).isEqualTo(5);
        assertThat(mozes.getXpToInvest()).isZero();
        assertThat(mozes.getLevel()).isEqualTo(1);
        assertThat(sb).isBlank();
        mozes.gainXp(10, sb);
        assertThat(mozes.getTotalXp()).isEqualTo(15);
        assertThat(mozes.getXpToInvest()).isEqualTo(10);
        assertThat(mozes.getLevel()).isEqualTo(1);
        assertThat(sb.toString().strip()).isBlank();
    }

    @Test
    void whenHeroGainsEnoughXp_ShouldGainLevel() {
        final HeroItem mozes = party.getHero("mozes");
        final StringBuilder sb = new StringBuilder();
        assertThat(mozes.getTotalXp()).isEqualTo(5);
        assertThat(mozes.getXpToInvest()).isZero();
        assertThat(mozes.getLevel()).isEqualTo(1);
        assertThat(sb).isBlank();
        mozes.gainXp(20, sb);
        assertThat(mozes.getTotalXp()).isEqualTo(25);
        assertThat(mozes.getXpToInvest()).isEqualTo(20);
        assertThat(mozes.getLevel()).isEqualTo(2);
        assertThat(sb.toString().strip()).isEqualTo("Mozes gained a level!");
    }

}
