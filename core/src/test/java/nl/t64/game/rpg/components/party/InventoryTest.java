package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.constants.ThreeState;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class InventoryTest extends GameTest {

    private static final String GOLD = "gold";
    private static final String BASIC_MACE = "basic_mace";
    private static final String BASIC_LIGHT_CHEST = "basic_light_chest";

    private InventoryContainer inventory;

    @BeforeEach
    private void setup() {
        final var profileManager = new ProfileManager();
        final var gameData = new GameData();
        gameData.onNotifyCreateProfile(profileManager);
        inventory = gameData.getInventory();
    }

    @Test
    void whenResourceItemIsCreated_ShouldHaveVariables() {
        final InventoryItem gold = InventoryDatabase.getInstance().getInventoryItem(GOLD);
        assertThat(gold.name).isEqualTo("Gold");
        assertThat(gold.getGroup()).isEqualTo(InventoryGroup.RESOURCE);
        assertThat(gold.getDescription()).isEqualTo("Gold can be used to pay for goods or services.");
    }

    @Test
    void whenInventoryItemIsCreated_ShouldHaveTradeValue() {
        InventoryItem basicMace = InventoryDatabase.getInstance().getInventoryItem(BASIC_MACE);
        assertThat(basicMace.price).isEqualTo(1);
        assertThat(basicMace.getSellValue()).isEqualTo(0);
        InventoryItem ordinaryMace = InventoryDatabase.getInstance().getInventoryItem("ordinary_mace");
        assertThat(ordinaryMace.price).isEqualTo(3);
        assertThat(ordinaryMace.getSellValue()).isEqualTo(1);
        InventoryItem specialistMace = InventoryDatabase.getInstance().getInventoryItem("specialist_mace");
        assertThat(specialistMace.price).isEqualTo(5);
        assertThat(specialistMace.getSellValue()).isEqualTo(1);
        InventoryItem masterworkMace = InventoryDatabase.getInstance().getInventoryItem("masterwork_mace");
        assertThat(masterworkMace.price).isEqualTo(7);
        assertThat(masterworkMace.getSellValue()).isEqualTo(2);
    }

    @Test
    void whenDataIsCreated_ShouldContainItems() {
        assertThat(inventory.getNumberOfFilledSlots()).isEqualTo(2);

        assertThat(inventory.contains(BASIC_MACE)).isTrue();
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getItemAt(0)).get().hasFieldOrPropertyWithValue("id", BASIC_MACE);

        assertThat(inventory.contains(GOLD)).isTrue();
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(1);
        assertThat(inventory.getItemAt(inventory.getLastIndex()))
                .get().hasFieldOrPropertyWithValue("id", GOLD);
    }

    @Test
    void whenResourceItemIsAdded_ShouldBeAddedToEndOfInventory() {
        String itemId = "herbs";
        InventoryItem herbs = InventoryDatabase.getInstance().getInventoryItem(itemId);
        assertThat(inventory.contains(itemId)).isFalse();
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex() - 1)).isEqualTo(0);
        inventory.autoSetItem(herbs);
        assertThat(inventory.contains(itemId)).isTrue();
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex() - 1)).isEqualTo(1);
        assertThat(inventory.getItemAt(inventory.getLastIndex() - 1))
                .get().hasFieldOrPropertyWithValue("id", itemId);
    }

    @Test
    void whenEquipmentItemIsAdded_ShouldBeAddedToStartOfInventory() {
        InventoryItem chest = InventoryDatabase.getInstance().getInventoryItem(BASIC_LIGHT_CHEST);
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isFalse();
        inventory.autoSetItem(chest);
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isTrue();
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getItemAt(1)).get().hasFieldOrPropertyWithValue("id", BASIC_LIGHT_CHEST);
    }

    @Test
    void whenItemIsForceSet_ShouldOverwriteExistingItem() {
        InventoryItem chest = InventoryDatabase.getInstance().getInventoryItem(BASIC_LIGHT_CHEST);
        assertThat(inventory.contains(BASIC_MACE)).isTrue();
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isFalse();

        inventory.forceSetItemAt(0, chest);

        assertThat(inventory.contains(BASIC_MACE)).isFalse();
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isTrue();
    }

    @Test
    void whenSameResourceItemIsAdded_ShouldIncreaseAmount() {
        InventoryItem gold = InventoryDatabase.getInstance().getInventoryItem(GOLD);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(1);
        inventory.autoSetItem(gold);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(2);
    }

    @Test
    void whenSameEquipmentItemIsAdded_ShouldNotIncreaseAmount() {
        InventoryItem basicMace = InventoryDatabase.getInstance().getInventoryItem(BASIC_MACE);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(0);
        inventory.autoSetItem(basicMace);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
    }

    @Test
    void whenSameResourceItemOfMultipleIsAdded_ShouldIncreaseTheFirstAmount() {
        InventoryItem gold1 = InventoryDatabase.getInstance().getInventoryItem(GOLD);
        InventoryItem gold2 = InventoryDatabase.getInstance().getInventoryItem(GOLD);
        InventoryItem gold3 = InventoryDatabase.getInstance().getInventoryItem(GOLD);
        inventory.forceSetItemAt(0, gold1);
        inventory.forceSetItemAt(1, gold2);
        inventory.forceSetItemAt(2, gold3);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(2)).isEqualTo(1);

        InventoryItem moreGold = InventoryDatabase.getInstance().getInventoryItem(GOLD);
        inventory.autoSetItem(moreGold);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(2);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(2)).isEqualTo(1);
    }

    @Test
    void whenInventoryIsFull_ShouldNotThrowError() {
        InventoryItem gold = InventoryDatabase.getInstance().getInventoryItem(GOLD);
        InventoryItem herbs = InventoryDatabase.getInstance().getInventoryItem("herbs");
        IntStream.range(0, inventory.getSize())
                 .forEach(i -> inventory.forceSetItemAt(i, gold));
        inventory.autoSetItem(herbs);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void whenInventoryItemWeapon_ShouldCreateDescription() {
        InventoryItem weapon = InventoryDatabase.getInstance().getInventoryItem(BASIC_MACE);
        HeroItem heroMock = Mockito.mock(HeroItem.class);
        Mockito.when(heroMock.getSkillById(SkillItemId.HAFTED)).thenReturn(new Hafted(1));
        Mockito.when(heroMock.getStatById(StatItemId.STRENGTH)).thenReturn(new Strength(1));
        List<InventoryDescription> description = new DescriptionCreator(weapon).createItemDescriptionComparingToHero(heroMock);
        assertThat(description.get(0).key).isEqualTo(InventoryGroup.WEAPON);
        assertThat(description.get(0).value).isEqualTo("Basic Mace");
        assertThat(description.get(1).key).isEqualTo("Price");
        assertThat(description.get(1).value).isEqualTo(1);
        assertThat(description.get(2).key).isEqualTo("Value");
        assertThat(description.get(2).value).isEqualTo(0);
        assertThat(description.get(3).key).isEqualTo(InventoryMinimal.SKILL);
        assertThat(description.get(3).value).isEqualTo(SkillItemId.HAFTED);
        assertThat(description.get(4).key).isEqualTo(InventoryMinimal.MIN_STRENGTH);
        assertThat(description.get(4).value).isEqualTo(15);
        assertThat(description.get(5).key).isEqualTo(CalcAttributeId.BASE_HIT);
        assertThat(description.get(5).value).isEqualTo(30);
        assertThat(description.get(6).key).isEqualTo(CalcAttributeId.DAMAGE);
        assertThat(description.get(6).value).isEqualTo(18);
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> description.get(7));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void whenInventoryItemShield_ShouldCreateDescription() {
        InventoryItem weapon = InventoryDatabase.getInstance().getInventoryItem("basic_light_shield");
        HeroItem heroMock = Mockito.mock(HeroItem.class);
        Mockito.when(heroMock.getSkillById(SkillItemId.SHIELD)).thenReturn(new Shield(1));
        Mockito.when(heroMock.getStatById(StatItemId.STRENGTH)).thenReturn(new Strength(1));
        List<InventoryDescription> description = new DescriptionCreator(weapon).createItemDescriptionComparingToHero(heroMock);
        assertThat(description.get(0).key).isEqualTo(InventoryGroup.SHIELD);
        assertThat(description.get(0).value).isEqualTo("Basic Light Shield");
        assertThat(description.get(1).key).isEqualTo("Price");
        assertThat(description.get(1).value).isEqualTo(1);
        assertThat(description.get(2).key).isEqualTo("Value");
        assertThat(description.get(2).value).isEqualTo(0);
        assertThat(description.get(3).key).isEqualTo(InventoryMinimal.SKILL);
        assertThat(description.get(3).value).isEqualTo(SkillItemId.SHIELD);
        assertThat(description.get(4).key).isEqualTo(InventoryMinimal.MIN_STRENGTH);
        assertThat(description.get(4).value).isEqualTo(14);
        assertThat(description.get(5).key).isEqualTo(CalcAttributeId.PROTECTION);
        assertThat(description.get(5).value).isEqualTo(1);
        assertThat(description.get(6).key).isEqualTo(CalcAttributeId.DEFENSE);
        assertThat(description.get(6).value).isEqualTo(5);
        assertThat(description.get(7).key).isEqualTo(StatItemId.DEXTERITY);
        assertThat(description.get(7).value).isEqualTo(-2);
        assertThat(description.get(8).key).isEqualTo(SkillItemId.STEALTH);
        assertThat(description.get(8).value).isEqualTo(-5);
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> description.get(9));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void whenInventoryItemChest_ShouldCreateDescription() {
        InventoryItem weapon = InventoryDatabase.getInstance().getInventoryItem(BASIC_LIGHT_CHEST);
        HeroItem heroMock = Mockito.mock(HeroItem.class);
        List<InventoryDescription> description = new DescriptionCreator(weapon).createItemDescriptionComparingToHero(heroMock);
        assertThat(description.get(0).key).isEqualTo(InventoryGroup.CHEST);
        assertThat(description.get(0).value).isEqualTo("Basic Light Chest");
        assertThat(description.get(1).key).isEqualTo("Price");
        assertThat(description.get(1).value).isEqualTo(1);
        assertThat(description.get(2).key).isEqualTo("Value");
        assertThat(description.get(2).value).isEqualTo(0);
        assertThat(description.get(3).key).isEqualTo(CalcAttributeId.WEIGHT);
        assertThat(description.get(3).value).isEqualTo(1);
        assertThat(description.get(4).key).isEqualTo(CalcAttributeId.PROTECTION);
        assertThat(description.get(4).value).isEqualTo(1);
        assertThat(description.get(5).key).isEqualTo(SkillItemId.STEALTH);
        assertThat(description.get(5).value).isEqualTo(0);
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> description.get(6));
    }

    @Test
    void whenItemIsComparedToHero_ShouldReturnSpecificThreeStates() {
        InventoryItem weapon = InventoryDatabase.getInstance().getInventoryItem(BASIC_MACE);
        HeroItem heroMock = Mockito.mock(HeroItem.class);
        Mockito.when(heroMock.getSkillById(SkillItemId.HAFTED)).thenReturn(new Hafted(1));
        Mockito.when(heroMock.getStatById(StatItemId.STRENGTH)).thenReturn(new Strength(10));
        List<InventoryDescription> description = new DescriptionCreator(weapon).createItemDescriptionComparingToHero(heroMock);
        assertThat(description.get(3).key).isEqualTo(InventoryMinimal.SKILL);
        assertThat(description.get(3).compare).isEqualTo(ThreeState.SAME);
        assertThat(description.get(4).key).isEqualTo(InventoryMinimal.MIN_STRENGTH);
        assertThat(description.get(4).compare).isEqualTo(ThreeState.LESS);
    }

    @Test
    void whenWeaponIsComparedToOtherWeapon_ShouldReturnSpecificThreeStates() {
        InventoryItem mace = InventoryDatabase.getInstance().getInventoryItem(BASIC_MACE);
        InventoryItem sword = InventoryDatabase.getInstance().getInventoryItem("basic_shortsword");
        List<InventoryDescription> description = new DescriptionCreator(mace).createItemDescriptionComparingToItem(sword);
        assertThat(description.get(5).key).isEqualTo(CalcAttributeId.BASE_HIT);
        assertThat(description.get(5).compare).isEqualTo(ThreeState.LESS);
        assertThat(description.get(6).key).isEqualTo(CalcAttributeId.DAMAGE);
        assertThat(description.get(6).compare).isEqualTo(ThreeState.MORE);
    }

    @Test
    void whenShieldIsComparedToOtherShield_ShouldReturnSpecificThreeStates() {
        InventoryItem light = InventoryDatabase.getInstance().getInventoryItem("basic_light_shield");
        InventoryItem medium = InventoryDatabase.getInstance().getInventoryItem("basic_medium_shield");
        List<InventoryDescription> description1 = new DescriptionCreator(light).createItemDescriptionComparingToItem(medium);
        assertThat(description1.get(7).key).isEqualTo(StatItemId.DEXTERITY);
        assertThat(description1.get(7).compare).isEqualTo(ThreeState.MORE);
        assertThat(description1.get(8).key).isEqualTo(SkillItemId.STEALTH);
        assertThat(description1.get(8).compare).isEqualTo(ThreeState.MORE);
        List<InventoryDescription> description2 = new DescriptionCreator(medium).createItemDescriptionComparingToItem(light);
        assertThat(description2.get(7).key).isEqualTo(StatItemId.DEXTERITY);
        assertThat(description2.get(7).compare).isEqualTo(ThreeState.LESS);
        assertThat(description2.get(8).key).isEqualTo(SkillItemId.STEALTH);
        assertThat(description2.get(8).compare).isEqualTo(ThreeState.LESS);
    }

}
