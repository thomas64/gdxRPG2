package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class InventoryTest extends GameTest {

    private static final String GOLD = "gold";
    private static final String HERB = "herb";
    private static final String POTION = "healing_potion";
    private static final String BASIC_MACE = "basic_mace";
    private static final String BASIC_LIGHT_CHEST = "basic_light_chest";

    private InventoryContainer inventory;

    @BeforeEach
    private void setup() {
        inventory = new InventoryContainer();
        InventoryItem mace = InventoryDatabase.createInventoryItem(BASIC_MACE);
        inventory.autoSetItem(mace);
        InventoryItem gold = InventoryDatabase.createInventoryItem(GOLD);
        inventory.autoSetItem(gold);
        inventory.sort();
    }

    @Test
    void whenResourceItemIsCreated_ShouldHaveVariables() {
        InventoryItem gold = InventoryDatabase.createInventoryItem(GOLD);
        assertThat(gold.name).isEqualTo("Gold");
        assertThat(gold.sort).isEqualTo(90090);
        assertThat(gold.getGroup()).isEqualTo(InventoryGroup.RESOURCE);
        assertThat(gold.getDescription()).isEqualTo(
                List.of("Gold can be used to pay for goods or services."));
    }

    @Test
    void whenPotionItemIsCreated_ShouldHaveVariables() {
        InventoryItem potion = InventoryDatabase.createInventoryItem(POTION);
        assertThat(potion.name).isEqualTo("Healing Potion");
        assertThat(potion.sort).isEqualTo(80005);
        assertThat(potion.getGroup()).isEqualTo(InventoryGroup.POTION);
        assertThat(potion.getDescription()).isEqualTo(
                List.of("Restores a fifth of the drinker's lost Endurance and Stamina.",
                        "Creating a Healing Potion requires 3 Herbs",
                        "and an Alchemist rank of at least 1."));
    }

    @Test
    void whenCopyOfItemIsCreated_ShouldBeSameItemButNotSameObject() {
        InventoryItem potion = InventoryDatabase.createInventoryItem(POTION);
        InventoryItem copyPotion = InventoryItem.copyOf(potion, potion.amount);
        assertThat(potion.hasSameIdAs(copyPotion)).isTrue();
        assertThat(potion).isNotEqualTo(copyPotion);
    }

    @Test
    void whenResourceItemIsCreatedWithAmount_ShouldHaveCorrectAmountWithoutSet() {
        InventoryItem spice = InventoryDatabase.createInventoryItem("spice");
        InventoryItem gold = InventoryDatabase.createInventoryItem(GOLD, 5);
        InventoryItem herb = InventoryDatabase.createInventoryItemForShop(HERB);
        InventoryItem potion = InventoryDatabase.createInventoryItemForShop(POTION);
        assertThat(spice.getAmount()).isEqualTo(1);
        assertThat(gold.getAmount()).isEqualTo(5);
        assertThat(herb.getAmount()).isEqualTo(100);
        assertThat(potion.getAmount()).isEqualTo(20);
    }

    @Test
    void whenInventoryItemIsCreated_ShouldHaveTradeValue() {
        InventoryItem basicMace = InventoryDatabase.createInventoryItem(BASIC_MACE);
        assertThat(basicMace.getBuyPriceTotal(0)).isEqualTo(15);
        assertThat(basicMace.getSellValueTotal(0)).isEqualTo(5);
        assertThat(basicMace.getBuyPriceTotal(30)).isEqualTo(11);
        assertThat(basicMace.getSellValueTotal(30)).isEqualTo(6);
        assertThat(basicMace.getBuyPriceTotal(60)).isEqualTo(6);
        assertThat(basicMace.getSellValueTotal(60)).isEqualTo(6);
        assertThat(basicMace.getBuyPriceTotal(100)).isEqualTo(1);
        assertThat(basicMace.getSellValueTotal(100)).isEqualTo(7);
        InventoryItem fineMace = InventoryDatabase.createInventoryItem("fine_mace");
        assertThat(fineMace.getBuyPriceTotal(0)).isEqualTo(45);
        assertThat(fineMace.getSellValueTotal(0)).isEqualTo(15);
        InventoryItem specialistMace = InventoryDatabase.createInventoryItem("specialist_mace");
        assertThat(specialistMace.getBuyPriceTotal(0)).isEqualTo(75);
        assertThat(specialistMace.getSellValueTotal(0)).isEqualTo(25);
        InventoryItem masterworkMace = InventoryDatabase.createInventoryItem("masterwork_mace");
        assertThat(masterworkMace.getBuyPriceTotal(0)).isEqualTo(105);
        assertThat(masterworkMace.getSellValueTotal(0)).isEqualTo(35);
        InventoryItem potion = InventoryDatabase.createInventoryItem(POTION);
        assertThat(potion.getBuyPricePiece(0)).isEqualTo(4);
        assertThat(potion.getBuyPriceTotal(0)).isEqualTo(4);
        assertThat(potion.getSellValuePiece(0)).isEqualTo(1);
        assertThat(potion.getSellValueTotal(0)).isEqualTo(1);
        potion.setAmount(20);
        assertThat(potion.getBuyPricePiece(0)).isEqualTo(4);
        assertThat(potion.getBuyPriceTotal(0)).isEqualTo(80);
        assertThat(potion.getSellValuePiece(0)).isEqualTo(1);
        assertThat(potion.getSellValueTotal(0)).isEqualTo(20);
    }

    @Test
    void whenDataIsCreated_ShouldContainItems() {
        assertThat(inventory.getNumberOfFilledSlots()).isEqualTo(2);

        assertThat(inventory.contains(BASIC_MACE)).isTrue();
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getItemAt(0)).hasFieldOrPropertyWithValue("id", BASIC_MACE);

        assertThat(inventory.contains(GOLD)).isTrue();
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(1);
        assertThat(inventory.getItemAt(inventory.getLastIndex())).hasFieldOrPropertyWithValue("id", GOLD);
    }

    @Test
    void whenDataIsCreated_ShouldContainItems2() {
        Map<String, Integer> map = Map.of(BASIC_MACE, 1, GOLD, 1);
        assertThat(inventory.contains(map)).isTrue();
        assertThat(inventory.contains(Map.of())).isFalse();
    }

    @Test
    void whenResourceItemIsAdded_ShouldBeAddedToStartOfInventory() {
        InventoryItem herb = InventoryDatabase.createInventoryItem(HERB);
        assertThat(inventory.contains(HERB)).isFalse();
        assertThat(inventory.getAmountOfItemAt(1)).isZero();
        inventory.autoSetItem(herb);
        assertThat(inventory.contains(HERB)).isTrue();
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getItemAt(1)).isSameAs(herb);
    }

    @Test
    void whenEquipmentItemIsAdded_ShouldBeAddedToStartOfInventory() {
        InventoryItem chest = InventoryDatabase.createInventoryItem(BASIC_LIGHT_CHEST);
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isFalse();
        inventory.autoSetItem(chest);
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isTrue();
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getItemAt(1)).isSameAs(chest);
    }

    @Test
    void whenInventoryDoesNotHoldEnoughOfResourceWhenRemoving_ShouldThrowException() {
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(
                () -> inventory.autoRemoveItem(GOLD, 2));
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(
                () -> inventory.autoRemoveItem(HERB, 1));
    }

    @Test
    void whenLargeQuantityResourceItemIsRemoved_ShouldRemoveFromMultipleSlots() {
        InventoryItem gold1 = InventoryDatabase.createInventoryItem(GOLD);
        InventoryItem gold2 = InventoryDatabase.createInventoryItem(GOLD);
        InventoryItem gold3 = InventoryDatabase.createInventoryItem(GOLD);
        InventoryItem gold4 = InventoryDatabase.createInventoryItem(GOLD);
        gold1.setAmount(15);
        gold2.setAmount(25);
        gold3.setAmount(35);
        gold4.setAmount(45);
        inventory.forceSetItemAt(1, gold1);
        inventory.forceSetItemAt(2, gold2);
        inventory.forceSetItemAt(3, gold3);
        inventory.forceSetItemAt(4, gold4);
        assertThat(inventory.getTotalOfItem(GOLD)).isEqualTo(121);

        inventory.autoRemoveItem(GOLD, 46);
        assertThat(inventory.getTotalOfItem(GOLD)).isEqualTo(75);
    }

    @Test
    void whenInventoryIsFull_ShouldAcceptSameResourceButRejectNewResource() {
        InventoryItem gold = InventoryDatabase.createInventoryItem(GOLD);
        IntStream.range(2, inventory.getSize())
                 .forEach(i -> inventory.forceSetItemAt(i, gold));
        assertThat(inventory.hasRoomForResource(GOLD)).isTrue();
        assertThat(inventory.hasRoomForResource(HERB)).isTrue();
        inventory.forceSetItemAt(1, gold);
        assertThat(inventory.hasRoomForResource(GOLD)).isTrue();
        assertThat(inventory.hasRoomForResource(HERB)).isFalse();
    }

    @Test
    void whenItemIsForceSet_ShouldOverwriteExistingItem() {
        InventoryItem chest = InventoryDatabase.createInventoryItem(BASIC_LIGHT_CHEST);
        assertThat(inventory.contains(BASIC_MACE)).isTrue();
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isFalse();

        inventory.forceSetItemAt(0, chest);

        assertThat(inventory.contains(BASIC_MACE)).isFalse();
        assertThat(inventory.contains(BASIC_LIGHT_CHEST)).isTrue();
    }

    @Test
    void whenSameResourceItemIsAdded_ShouldIncreaseAmount() {
        InventoryItem gold = InventoryDatabase.createInventoryItem(GOLD);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(1);
        inventory.autoSetItem(gold);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(2);
    }

    @Test
    void whenSameEquipmentItemIsAdded_ShouldNotIncreaseAmount() {
        InventoryItem basicMace = InventoryDatabase.createInventoryItem(BASIC_MACE);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isZero();
        inventory.autoSetItem(basicMace);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
    }

    @Test
    void whenSameResourceItemOfMultipleIsAdded_ShouldIncreaseTheFirstAmount() {
        InventoryItem gold1 = InventoryDatabase.createInventoryItem(GOLD);
        InventoryItem gold2 = InventoryDatabase.createInventoryItem(GOLD);
        InventoryItem gold3 = InventoryDatabase.createInventoryItem(GOLD);
        inventory.forceSetItemAt(0, gold1);
        inventory.forceSetItemAt(1, gold2);
        inventory.forceSetItemAt(2, gold3);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(2)).isEqualTo(1);

        InventoryItem moreGold = InventoryDatabase.createInventoryItem(GOLD);
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
        assertThat(inventory.findFirstSlotWithItem(GOLD)).isEqualTo(65);
        assertThat(inventory.findFirstSlotWithItem(HERB)).isNull();
    }

    @Test
    void whenCheckingForAmountOfResourceIsEnough_ShouldTrueOrFalse() {
        assertThat(inventory.hasEnoughOfItem(GOLD, 2)).isFalse();
        assertThat(inventory.hasEnoughOfItem(GOLD, 1)).isTrue();
    }

    @Test
    void whenCheckingSomeLeftoverMethodsInInventory_ShouldSucceed() {
        assertThat(inventory.getAllFilledSlots()).hasSize(2);
        assertThat(inventory.isEmpty()).isFalse();
        inventory.clearItemAt(0);
        inventory.clearItemAt(inventory.getLastIndex());
        assertThat(inventory.isEmpty()).isTrue();
        inventory.forceSetItemAt(8, new InventoryItem());
        assertThat(inventory.findFirstFilledSlot()).isEqualTo(8);
    }

    @Test
    void whenInventoryIsSorted_ShouldBeSortedAndMerged() {
        InventoryItem gold1 = InventoryDatabase.createInventoryItem(GOLD);
        InventoryItem gold2 = InventoryDatabase.createInventoryItem(GOLD);
        InventoryItem gold3 = InventoryDatabase.createInventoryItem(GOLD);
        InventoryItem herb1 = InventoryDatabase.createInventoryItem(HERB);
        InventoryItem herb2 = InventoryDatabase.createInventoryItem(HERB);
        InventoryItem herb3 = InventoryDatabase.createInventoryItem(HERB);
        herb1.setAmount(80);
        herb2.setAmount(80);
        InventoryItem chest = InventoryDatabase.createInventoryItem(BASIC_LIGHT_CHEST);

        inventory.forceSetItemAt(0, gold1);
        inventory.forceSetItemAt(20, gold2);
        inventory.forceSetItemAt(65, gold3);
        inventory.forceSetItemAt(10, herb1);
        inventory.forceSetItemAt(40, herb2);
        inventory.forceSetItemAt(50, herb3);
        inventory.forceSetItemAt(36, chest);

        inventory.sort();
        inventory.sort();

        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getItemAt(0)).isSameAs(chest);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(3);
        assertThat(inventory.getItemAt(inventory.getLastIndex())).isSameAs(gold1);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex() - 1)).isEqualTo(161);
        assertThat(inventory.getItemAt(inventory.getLastIndex() - 1)).isSameAs(herb1);
    }

    @Disabled
    @Test
    void whenInventoryIsFull_ShouldNotThrowError() {
        InventoryItem gold = InventoryDatabase.createInventoryItem(GOLD);
        InventoryItem herb = InventoryDatabase.createInventoryItem(HERB);
        InventoryItem mace = InventoryDatabase.createInventoryItem(BASIC_MACE);
        IntStream.range(0, inventory.getSize())
                 .forEach(i -> inventory.forceSetItemAt(i, gold));
        inventory.autoSetItem(herb);
        inventory.autoSetItem(mace);
    }

    @Test
    void whenEmptyIndexIsIncreased_ShouldThrowException() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> inventory.incrementAmountAt(2, 1));
    }

    @Test
    void whenInventoryItemWeapon_ShouldCreateDescription() {
        InventoryItem weapon = InventoryDatabase.createInventoryItem(BASIC_MACE);
        List<InventoryDescription> description = new DescriptionCreator(weapon, 0)
                .createItemDescription();
        assertThat(description.get(0).getKey()).isEqualTo(InventoryGroup.WEAPON);
        assertThat(description.get(0).getValue()).isEqualTo("Basic Mace");
        assertThat(description.get(1).getKey()).isEqualTo("(One-handed)");
        assertThat(description.get(1).getValue()).isEqualTo("");
        assertThat(description.get(2).getKey()).isEqualTo("Price");
        assertThat(description.get(2).getValue()).isEqualTo(15);
        assertThat(description.get(3).getKey()).isEqualTo("Sell value");
        assertThat(description.get(3).getValue()).isEqualTo(5);
        assertThat(description.get(4).getKey()).isEqualTo(InventoryMinimal.SKILL);
        assertThat(description.get(4).getValue()).isEqualTo(SkillItemId.HAFTED);
        assertThat(description.get(5).getKey()).isEqualTo(InventoryMinimal.MIN_STRENGTH);
        assertThat(description.get(5).getValue()).isEqualTo(15);
        assertThat(description.get(6).getKey()).isEqualTo(CalcAttributeId.BASE_HIT);
        assertThat(description.get(6).getValue()).isEqualTo(30);
        assertThat(description.get(7).getKey()).isEqualTo(CalcAttributeId.DAMAGE);
        assertThat(description.get(7).getValue()).isEqualTo(18);
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> description.get(8));
    }

    @Test
    void whenInventoryItemWeaponTwoHanded_ShouldCreateDescription() {
        InventoryItem weapon = InventoryDatabase.createInventoryItem("basic_shortbow");
        List<InventoryDescription> description = new DescriptionCreator(weapon, 0)
                .createItemDescription();
        assertThat(description.get(0).getKey()).isEqualTo(InventoryGroup.WEAPON);
        assertThat(description.get(0).getValue()).isEqualTo("Basic Shortbow");
        assertThat(description.get(1).getKey()).isEqualTo("(Two-handed)");
        assertThat(description.get(1).getValue()).isEqualTo("");
    }

    @Test
    void whenInventoryItemShield_ShouldCreateDescription() {
        InventoryItem weapon = InventoryDatabase.createInventoryItem("basic_light_shield");
        List<InventoryDescription> description = new DescriptionCreator(weapon, 0)
                .createItemDescription();
        assertThat(description.get(0).getKey()).isEqualTo(InventoryGroup.SHIELD);
        assertThat(description.get(0).getValue()).isEqualTo("Basic Light Shield");
        assertThat(description.get(1).getKey()).isEqualTo("Price");
        assertThat(description.get(1).getValue()).isEqualTo(8);
        assertThat(description.get(2).getKey()).isEqualTo("Sell value");
        assertThat(description.get(2).getValue()).isEqualTo(2);
        assertThat(description.get(3).getKey()).isEqualTo(InventoryMinimal.SKILL);
        assertThat(description.get(3).getValue()).isEqualTo(SkillItemId.SHIELD);
        assertThat(description.get(4).getKey()).isEqualTo(InventoryMinimal.MIN_STRENGTH);
        assertThat(description.get(4).getValue()).isEqualTo(14);
        assertThat(description.get(5).getKey()).isEqualTo(CalcAttributeId.PROTECTION);
        assertThat(description.get(5).getValue()).isEqualTo(1);
        assertThat(description.get(6).getKey()).isEqualTo(CalcAttributeId.DEFENSE);
        assertThat(description.get(6).getValue()).isEqualTo(5);
        assertThat(description.get(7).getKey()).isEqualTo(StatItemId.AGILITY);
        assertThat(description.get(7).getValue()).isEqualTo(0);
        assertThat(description.get(8).getKey()).isEqualTo(SkillItemId.STEALTH);
        assertThat(description.get(8).getValue()).isEqualTo(-5);
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> description.get(9));
    }

    @Test
    void whenInventoryItemChest_ShouldCreateDescription() {
        InventoryItem weapon = InventoryDatabase.createInventoryItem(BASIC_LIGHT_CHEST);
        List<InventoryDescription> description = new DescriptionCreator(weapon, 0)
                .createItemDescription();
        assertThat(description.get(0).getKey()).isEqualTo(InventoryGroup.CHEST);
        assertThat(description.get(0).getValue()).isEqualTo("Basic Light Chest");
        assertThat(description.get(1).getKey()).isEqualTo("Price");
        assertThat(description.get(1).getValue()).isEqualTo(10);
        assertThat(description.get(2).getKey()).isEqualTo("Sell value");
        assertThat(description.get(2).getValue()).isEqualTo(3);
        assertThat(description.get(3).getKey()).isEqualTo(CalcAttributeId.WEIGHT);
        assertThat(description.get(3).getValue()).isEqualTo(1);
        assertThat(description.get(4).getKey()).isEqualTo(CalcAttributeId.PROTECTION);
        assertThat(description.get(4).getValue()).isEqualTo(1);
        assertThat(description.get(5).getKey()).isEqualTo(StatItemId.AGILITY);
        assertThat(description.get(5).getValue()).isEqualTo(0);
        assertThat(description.get(6).getKey()).isEqualTo(SkillItemId.STEALTH);
        assertThat(description.get(6).getValue()).isEqualTo(0);
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> description.get(7));
    }

    @Test
    void whenResourceItemHerb_ShouldCreateDescription() {
        InventoryItem potion = InventoryDatabase.createInventoryItem(POTION, 10);
        List<InventoryDescription> description = new DescriptionCreator(potion, 0)
                .createItemDescription();
        assertThat(description.get(0).getKey()).isEqualTo(InventoryGroup.POTION);
        assertThat(description.get(0).getValue()).isEqualTo("Healing Potion");
        assertThat(description.get(1).getKey()).isEqualTo("Price per piece");
        assertThat(description.get(1).getValue()).isEqualTo(4);
        assertThat(description.get(2).getKey()).isEqualTo("Total price");
        assertThat(description.get(2).getValue()).isEqualTo(40);
        assertThat(description.get(3).getKey()).isEqualTo("Sell value per piece");
        assertThat(description.get(3).getValue()).isEqualTo(1);
        assertThat(description.get(4).getKey()).isEqualTo("Total sell value");
        assertThat(description.get(4).getValue()).isEqualTo(10);
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> description.get(5));
    }

    @Test
    void whenAmountOfItemIsZero_ShouldThrowException() {
        InventoryItem potion = InventoryDatabase.createInventoryItem(POTION, 0);
        var creator = new DescriptionCreator(potion, 0);
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(creator::createItemDescription);
    }

    @Test
    void whenItemIsComparedToHero_ShouldReturnSpecificThreeStates() {
        InventoryItem weapon = InventoryDatabase.createInventoryItem(BASIC_MACE);
        HeroItem heroMock = Mockito.mock(HeroItem.class);
        Mockito.when(heroMock.getSkillById(SkillItemId.HAFTED)).thenReturn(new Hafted(1));
        Mockito.when(heroMock.getStatById(StatItemId.STRENGTH)).thenReturn(new Strength(10));
        List<InventoryDescription> description = new DescriptionCreator(weapon, 0)
                .createItemDescriptionComparingToHero(heroMock);
        assertThat(description.get(4).getKey()).isEqualTo(InventoryMinimal.SKILL);
        assertThat(description.get(4).getCompare()).isEqualTo(ThreeState.SAME);
        assertThat(description.get(5).getKey()).isEqualTo(InventoryMinimal.MIN_STRENGTH);
        assertThat(description.get(5).getCompare()).isEqualTo(ThreeState.LESS);
    }

    @Test
    void whenWeaponIsComparedToOtherWeapon_ShouldReturnSpecificThreeStates() {
        InventoryItem mace = InventoryDatabase.createInventoryItem(BASIC_MACE);
        InventoryItem sword = InventoryDatabase.createInventoryItem("basic_shortsword");
        List<InventoryDescription> description = new DescriptionCreator(mace, 0)
                .createItemDescriptionComparingToItem(sword);
        assertThat(description.get(6).getKey()).isEqualTo(CalcAttributeId.BASE_HIT);
        assertThat(description.get(6).getCompare()).isEqualTo(ThreeState.LESS);
        assertThat(description.get(7).getKey()).isEqualTo(CalcAttributeId.DAMAGE);
        assertThat(description.get(7).getCompare()).isEqualTo(ThreeState.MORE);
    }

    @Test
    void whenWeaponIsComparedToOtherTypeWeapon_ShouldReturnEmptyMinimalLines() {
        InventoryItem mace = InventoryDatabase.createInventoryItem(BASIC_MACE);
        InventoryItem javelin = InventoryDatabase.createInventoryItem("basic_javelin");
        List<InventoryDescription> description = new DescriptionCreator(mace, 0)
                .createItemDescriptionComparingToItem(javelin);
        assertThat(description.get(4).getKey()).isEqualTo(InventoryMinimal.SKILL);
        assertThat(description.get(5).getKey()).isEqualTo(InventoryMinimal.MIN_STRENGTH);
        assertThat(description.get(6).getKey()).isEqualTo("");
        assertThat(description.get(7).getKey()).isEqualTo(CalcAttributeId.BASE_HIT);
    }

    @Test
    void whenShieldIsComparedToOtherShield_ShouldReturnSpecificThreeStates() {
        InventoryItem light = InventoryDatabase.createInventoryItem("basic_light_shield");
        InventoryItem medium = InventoryDatabase.createInventoryItem("basic_medium_shield");
        List<InventoryDescription> description1 = new DescriptionCreator(light, 0)
                .createItemDescriptionComparingToItem(medium);
        assertThat(description1.get(7).getKey()).isEqualTo(StatItemId.AGILITY);
        assertThat(description1.get(7).getCompare()).isEqualTo(ThreeState.MORE);
        assertThat(description1.get(8).getKey()).isEqualTo(SkillItemId.STEALTH);
        assertThat(description1.get(8).getCompare()).isEqualTo(ThreeState.MORE);
        List<InventoryDescription> description2 = new DescriptionCreator(medium, 0)
                .createItemDescriptionComparingToItem(light);
        assertThat(description2.get(7).getKey()).isEqualTo(StatItemId.AGILITY);
        assertThat(description2.get(7).getCompare()).isEqualTo(ThreeState.LESS);
        assertThat(description2.get(8).getKey()).isEqualTo(SkillItemId.STEALTH);
        assertThat(description2.get(8).getCompare()).isEqualTo(ThreeState.LESS);
    }

}
