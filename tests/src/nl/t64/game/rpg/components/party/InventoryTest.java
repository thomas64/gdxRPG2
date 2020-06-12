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
    private static final String HERBS = "herbs";
    private static final String POTION = "healing_potion";
    private static final String BASIC_MACE = "basic_mace";
    private static final String BASIC_LIGHT_CHEST = "basic_light_chest";

    private InventoryDatabase inventoryDB = InventoryDatabase.getInstance();
    private InventoryContainer inventory;

    @BeforeEach
    private void setup() {
        final var profileManager = new ProfileManager();
        final var gameData = new GameData();
        gameData.onNotifyCreateProfile(profileManager);
        inventoryDB = InventoryDatabase.getInstance();
        inventory = gameData.getInventory();
        inventory.sort();
    }

    @Test
    void whenResourceItemIsCreated_ShouldHaveVariables() {
        InventoryItem gold = inventoryDB.createInventoryItem(GOLD);
        assertThat(gold.name).isEqualTo("Gold");
        assertThat(gold.sort).isEqualTo(999);
        assertThat(gold.getGroup()).isEqualTo(InventoryGroup.RESOURCE);
        assertThat(gold.getDescription()).isEqualTo(
                List.of("Gold can be used to pay for goods or services."));
    }

    @Test
    void whenPotionItemIsCreated_ShouldHaveVariables() {
        InventoryItem potion = inventoryDB.createInventoryItem(POTION);
        assertThat(potion.name).isEqualTo("Healing Potion");
        assertThat(potion.sort).isEqualTo(5);
        assertThat(potion.getGroup()).isEqualTo(InventoryGroup.POTION);
        assertThat(potion.getDescription()).isEqualTo(
                List.of("Restores a fifth of the drinker's lost Endurance and Stamina.",
                        "Creating a Healing Potion requires 3 Herbs",
                        "and an Alchemist rank of at least 1."));
    }

    @Test
    void whenResourceItemIsCreatedWithAmount_ShouldHaveCorrectAmountWithoutSet() {
        InventoryItem spices = inventoryDB.createInventoryItem("spices");
        InventoryItem gold = inventoryDB.createInventoryItem(GOLD, 5);
        InventoryItem herbs = inventoryDB.createInventoryItemForShop(HERBS);
        InventoryItem potion = inventoryDB.createInventoryItemForShop(POTION);
        assertThat(spices.getAmount()).isEqualTo(1);
        assertThat(gold.getAmount()).isEqualTo(5);
        assertThat(herbs.getAmount()).isEqualTo(100);
        assertThat(potion.getAmount()).isEqualTo(20);
    }

    @Test
    void whenInventoryItemIsCreated_ShouldHaveTradeValue() {
        InventoryItem basicMace = inventoryDB.createInventoryItem(BASIC_MACE);
        assertThat(basicMace.getBuyPrice(0)).isEqualTo(15);
        assertThat(basicMace.getSellValue(0)).isEqualTo(5);
        assertThat(basicMace.getBuyPrice(30)).isEqualTo(11);
        assertThat(basicMace.getSellValue(30)).isEqualTo(6);
        assertThat(basicMace.getBuyPrice(60)).isEqualTo(6);
        assertThat(basicMace.getSellValue(60)).isEqualTo(6);
        assertThat(basicMace.getBuyPrice(100)).isEqualTo(1);
        assertThat(basicMace.getSellValue(100)).isEqualTo(7);
        InventoryItem ordinaryMace = inventoryDB.createInventoryItem("ordinary_mace");
        assertThat(ordinaryMace.getBuyPrice(0)).isEqualTo(45);
        assertThat(ordinaryMace.getSellValue(0)).isEqualTo(15);
        InventoryItem specialistMace = inventoryDB.createInventoryItem("specialist_mace");
        assertThat(specialistMace.getBuyPrice(0)).isEqualTo(75);
        assertThat(specialistMace.getSellValue(0)).isEqualTo(25);
        InventoryItem masterworkMace = inventoryDB.createInventoryItem("masterwork_mace");
        assertThat(masterworkMace.getBuyPrice(0)).isEqualTo(105);
        assertThat(masterworkMace.getSellValue(0)).isEqualTo(35);
        InventoryItem potion = inventoryDB.createInventoryItem(POTION);
        assertThat(potion.getBuyPrice(0)).isEqualTo(4);
        assertThat(potion.getSellValue(0)).isEqualTo(1);
        potion.setAmount(20);
        assertThat(potion.getBuyPrice(0)).isEqualTo(80);
        assertThat(potion.getSellValue(0)).isEqualTo(20);
    }

    @Test
    void whenDataIsCreated_ShouldContainItems() {
        assertThat(inventory.getNumberOfFilledSlots()).isEqualTo(2);

        assertThat(inventory.contains(BASIC_MACE)).isTrue();
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getItemAt(0)).get().hasFieldOrPropertyWithValue("id", BASIC_MACE);

        assertThat(inventory.contains(GOLD)).isTrue();
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(1);
        assertThat(inventory.getItemAt(inventory.getLastIndex())).get().hasFieldOrPropertyWithValue("id", GOLD);
    }

    @Test
    void whenResourceItemIsAdded_ShouldBeAddedToStartOfInventory() {
        InventoryItem herbs = inventoryDB.createInventoryItem(HERBS);
        assertThat(inventory.contains(HERBS)).isFalse();
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(0);
        inventory.autoSetItem(herbs);
        assertThat(inventory.contains(HERBS)).isTrue();
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getItemAt(1)).containsSame(herbs);
    }

    @Test
    void whenEquipmentItemIsAdded_ShouldBeAddedToStartOfInventory() {
        InventoryItem chest = inventoryDB.createInventoryItem(BASIC_LIGHT_CHEST);
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isFalse();
        inventory.autoSetItem(chest);
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isTrue();
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getItemAt(1)).containsSame(chest);
    }

    @Test
    void whenInventoryDoesNotHoldEnoughOfResourceWhenRemoving_ShouldThrowException() {
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(
                () -> inventory.autoRemoveResource(GOLD, 2));
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(
                () -> inventory.autoRemoveResource(HERBS, 1));
    }

    @Test
    void whenLargeQuantityResourceItemIsRemoved_ShouldRemoveFromMultipleSlots() {
        InventoryItem gold1 = inventoryDB.createInventoryItem(GOLD);
        InventoryItem gold2 = inventoryDB.createInventoryItem(GOLD);
        InventoryItem gold3 = inventoryDB.createInventoryItem(GOLD);
        InventoryItem gold4 = inventoryDB.createInventoryItem(GOLD);
        gold1.setAmount(15);
        gold2.setAmount(25);
        gold3.setAmount(35);
        gold4.setAmount(45);
        inventory.forceSetItemAt(1, gold1);
        inventory.forceSetItemAt(2, gold2);
        inventory.forceSetItemAt(3, gold3);
        inventory.forceSetItemAt(4, gold4);
        assertThat(inventory.getTotalOfResource(GOLD)).isEqualTo(121);

        inventory.autoRemoveResource(GOLD, 46);
        assertThat(inventory.getTotalOfResource(GOLD)).isEqualTo(75);
    }

    @Test
    void whenInventoryIsFull_ShouldAcceptSameResourceButRejectNewResource() {
        InventoryItem gold = inventoryDB.createInventoryItem(GOLD);
        IntStream.range(2, inventory.getSize())
                 .forEach(i -> inventory.forceSetItemAt(i, gold));
        assertThat(inventory.hasRoomForResource(GOLD)).isTrue();
        assertThat(inventory.hasRoomForResource(HERBS)).isTrue();
        inventory.forceSetItemAt(1, gold);
        assertThat(inventory.hasRoomForResource(GOLD)).isTrue();
        assertThat(inventory.hasRoomForResource(HERBS)).isFalse();
    }

    @Test
    void whenItemIsForceSet_ShouldOverwriteExistingItem() {
        InventoryItem chest = inventoryDB.createInventoryItem(BASIC_LIGHT_CHEST);
        assertThat(inventory.contains(BASIC_MACE)).isTrue();
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isFalse();

        inventory.forceSetItemAt(0, chest);

        assertThat(inventory.contains(BASIC_MACE)).isFalse();
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isTrue();
    }

    @Test
    void whenSameResourceItemIsAdded_ShouldIncreaseAmount() {
        InventoryItem gold = inventoryDB.createInventoryItem(GOLD);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(1);
        inventory.autoSetItem(gold);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(2);
    }

    @Test
    void whenSameEquipmentItemIsAdded_ShouldNotIncreaseAmount() {
        InventoryItem basicMace = inventoryDB.createInventoryItem(BASIC_MACE);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(0);
        inventory.autoSetItem(basicMace);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
    }

    @Test
    void whenSameResourceItemOfMultipleIsAdded_ShouldIncreaseTheFirstAmount() {
        InventoryItem gold1 = inventoryDB.createInventoryItem(GOLD);
        InventoryItem gold2 = inventoryDB.createInventoryItem(GOLD);
        InventoryItem gold3 = inventoryDB.createInventoryItem(GOLD);
        inventory.forceSetItemAt(0, gold1);
        inventory.forceSetItemAt(1, gold2);
        inventory.forceSetItemAt(2, gold3);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(2)).isEqualTo(1);

        InventoryItem moreGold = inventoryDB.createInventoryItem(GOLD);
        inventory.autoSetItem(moreGold);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(2);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(2)).isEqualTo(1);
    }

    @Test
    void whenInventoryIsIncreasedAndDecreased_ShouldIncreaseAndDecreaseAsSuch() {
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(1);
        inventory.incrementAmountAt(inventory.getLastIndex(), 5);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(6);
        inventory.decrementAmountAt(inventory.getLastIndex(), 3);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(3);
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(
                () -> inventory.decrementAmountAt(inventory.getLastIndex(), 3));
    }

    @Test
    void whenSearchingForSlotWithItem_ShouldReturnIndexOrNot() {
        assertThat(inventory.findFirstSlotWithItem(GOLD)).contains(65);
        assertThat(inventory.findFirstSlotWithItem(HERBS)).isEmpty();
    }

    @Test
    void whenCheckingForAmountOfResourceIsEnough_ShouldTrueOrFalse() {
        assertThat(inventory.hasEnoughOfResource(GOLD, 2)).isFalse();
        assertThat(inventory.hasEnoughOfResource(GOLD, 1)).isTrue();
    }

    @Test
    void whenCheckingSomeLeftoverMethodsInInventory_ShouldSucceed() {
        assertThat(inventory.getAllFilledSlots()).hasSize(2);
        assertThat(inventory.isEmpty()).isFalse();
        inventory.clearItemAt(0);
        inventory.clearItemAt(inventory.getLastIndex());
        assertThat(inventory.isEmpty()).isTrue();
        inventory.forceSetItemAt(8, new InventoryItem());
        assertThat(inventory.findFirstFilledSlot()).hasValue(8);
    }

    @Test
    void whenInventoryIsSorted_ShouldBeSortedAndMerged() {
        InventoryItem gold1 = inventoryDB.createInventoryItem(GOLD);
        InventoryItem gold2 = inventoryDB.createInventoryItem(GOLD);
        InventoryItem gold3 = inventoryDB.createInventoryItem(GOLD);
        InventoryItem herbs1 = inventoryDB.createInventoryItem(HERBS);
        InventoryItem herbs2 = inventoryDB.createInventoryItem(HERBS);
        InventoryItem herbs3 = inventoryDB.createInventoryItem(HERBS);
        herbs1.setAmount(80);
        herbs2.setAmount(80);
        InventoryItem chest = inventoryDB.createInventoryItem(BASIC_LIGHT_CHEST);

        inventory.forceSetItemAt(0, gold1);
        inventory.forceSetItemAt(20, gold2);
        inventory.forceSetItemAt(65, gold3);
        inventory.forceSetItemAt(10, herbs1);
        inventory.forceSetItemAt(40, herbs2);
        inventory.forceSetItemAt(50, herbs3);
        inventory.forceSetItemAt(36, chest);

        inventory.sort();

        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getItemAt(0)).containsSame(chest);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(3);
        assertThat(inventory.getItemAt(inventory.getLastIndex())).containsSame(gold1);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex() - 1)).isEqualTo(161);
        assertThat(inventory.getItemAt(inventory.getLastIndex() - 1)).containsSame(herbs1);
    }

    @Test
    void whenInventoryIsFull_ShouldNotThrowError() {
        InventoryItem gold = inventoryDB.createInventoryItem(GOLD);
        InventoryItem herbs = inventoryDB.createInventoryItem(HERBS);
        InventoryItem mace = inventoryDB.createInventoryItem(BASIC_MACE);
        IntStream.range(0, inventory.getSize())
                 .forEach(i -> inventory.forceSetItemAt(i, gold));
        inventory.autoSetItem(herbs);
        inventory.autoSetItem(mace);
    }

    @Test
    void whenInventoryItemWeapon_ShouldCreateDescription() {
        InventoryItem weapon = inventoryDB.createInventoryItem(BASIC_MACE);
        List<InventoryDescription> description = new DescriptionCreator(weapon, 0)
                .createItemDescription();
        assertThat(description.get(0).key).isEqualTo(InventoryGroup.WEAPON);
        assertThat(description.get(0).value).isEqualTo("Basic Mace");
        assertThat(description.get(1).key).isEqualTo("Price");
        assertThat(description.get(1).value).isEqualTo(15);
        assertThat(description.get(2).key).isEqualTo("Value");
        assertThat(description.get(2).value).isEqualTo(5);
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

    @Test
    void whenInventoryItemShield_ShouldCreateDescription() {
        InventoryItem weapon = inventoryDB.createInventoryItem("basic_light_shield");
        List<InventoryDescription> description = new DescriptionCreator(weapon, 0)
                .createItemDescription();
        assertThat(description.get(0).key).isEqualTo(InventoryGroup.SHIELD);
        assertThat(description.get(0).value).isEqualTo("Basic Light Shield");
        assertThat(description.get(1).key).isEqualTo("Price");
        assertThat(description.get(1).value).isEqualTo(8);
        assertThat(description.get(2).key).isEqualTo("Value");
        assertThat(description.get(2).value).isEqualTo(2);
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

    @Test
    void whenInventoryItemChest_ShouldCreateDescription() {
        InventoryItem weapon = inventoryDB.createInventoryItem(BASIC_LIGHT_CHEST);
        List<InventoryDescription> description = new DescriptionCreator(weapon, 0)
                .createItemDescription();
        assertThat(description.get(0).key).isEqualTo(InventoryGroup.CHEST);
        assertThat(description.get(0).value).isEqualTo("Basic Light Chest");
        assertThat(description.get(1).key).isEqualTo("Price");
        assertThat(description.get(1).value).isEqualTo(10);
        assertThat(description.get(2).key).isEqualTo("Value");
        assertThat(description.get(2).value).isEqualTo(3);
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
        InventoryItem weapon = inventoryDB.createInventoryItem(BASIC_MACE);
        HeroItem heroMock = Mockito.mock(HeroItem.class);
        Mockito.when(heroMock.getSkillById(SkillItemId.HAFTED)).thenReturn(new Hafted(1));
        Mockito.when(heroMock.getStatById(StatItemId.STRENGTH)).thenReturn(new Strength(10));
        List<InventoryDescription> description = new DescriptionCreator(weapon, 0)
                .createItemDescriptionComparingToHero(heroMock);
        assertThat(description.get(3).key).isEqualTo(InventoryMinimal.SKILL);
        assertThat(description.get(3).compare).isEqualTo(ThreeState.SAME);
        assertThat(description.get(4).key).isEqualTo(InventoryMinimal.MIN_STRENGTH);
        assertThat(description.get(4).compare).isEqualTo(ThreeState.LESS);
    }

    @Test
    void whenWeaponIsComparedToOtherWeapon_ShouldReturnSpecificThreeStates() {
        InventoryItem mace = inventoryDB.createInventoryItem(BASIC_MACE);
        InventoryItem sword = inventoryDB.createInventoryItem("basic_shortsword");
        List<InventoryDescription> description = new DescriptionCreator(mace, 0)
                .createItemDescriptionComparingToItem(sword);
        assertThat(description.get(5).key).isEqualTo(CalcAttributeId.BASE_HIT);
        assertThat(description.get(5).compare).isEqualTo(ThreeState.LESS);
        assertThat(description.get(6).key).isEqualTo(CalcAttributeId.DAMAGE);
        assertThat(description.get(6).compare).isEqualTo(ThreeState.MORE);
    }

    @Test
    void whenShieldIsComparedToOtherShield_ShouldReturnSpecificThreeStates() {
        InventoryItem light = inventoryDB.createInventoryItem("basic_light_shield");
        InventoryItem medium = inventoryDB.createInventoryItem("basic_medium_shield");
        List<InventoryDescription> description1 = new DescriptionCreator(light, 0)
                .createItemDescriptionComparingToItem(medium);
        assertThat(description1.get(7).key).isEqualTo(StatItemId.DEXTERITY);
        assertThat(description1.get(7).compare).isEqualTo(ThreeState.MORE);
        assertThat(description1.get(8).key).isEqualTo(SkillItemId.STEALTH);
        assertThat(description1.get(8).compare).isEqualTo(ThreeState.MORE);
        List<InventoryDescription> description2 = new DescriptionCreator(medium, 0)
                .createItemDescriptionComparingToItem(light);
        assertThat(description2.get(7).key).isEqualTo(StatItemId.DEXTERITY);
        assertThat(description2.get(7).compare).isEqualTo(ThreeState.LESS);
        assertThat(description2.get(8).key).isEqualTo(SkillItemId.STEALTH);
        assertThat(description2.get(8).compare).isEqualTo(ThreeState.LESS);
    }

}