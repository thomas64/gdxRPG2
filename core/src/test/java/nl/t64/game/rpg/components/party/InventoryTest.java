package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class InventoryTest extends GameTest {

    private GlobalContainer inventory;

    @BeforeEach
    private void setup() {
        final var profileManager = new ProfileManager();
        final var gameData = new GameData();
        gameData.onNotifyCreate(profileManager);
        inventory = gameData.getInventory();
    }

    @Test
    void whenDataIsCreated_ShouldContainItems() {
        assertThat(inventory.getNumberOfFilledSlots()).isEqualTo(2);

        assertThat(inventory.contains("basic_mace")).isTrue();
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getItemAt(0)).get().hasFieldOrPropertyWithValue("id", "basic_mace");

        assertThat(inventory.contains("gold")).isTrue();
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(1);
        assertThat(inventory.getItemAt(inventory.getLastIndex()))
                .get().hasFieldOrPropertyWithValue("id", "gold");
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
        String itemId = "basic_light_chest";
        InventoryItem chest = InventoryDatabase.getInstance().getInventoryItem(itemId);
        assertThat(inventory.contains(itemId)).isFalse();
        inventory.autoSetItem(chest);
        assertThat(inventory.contains(itemId)).isTrue();
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getItemAt(1)).get().hasFieldOrPropertyWithValue("id", itemId);
    }

    @Test
    void whenItemIsForceSet_ShouldOverwriteExistingItem() {
        InventoryItem chest = InventoryDatabase.getInstance().getInventoryItem("basic_light_chest");
        assertThat(inventory.contains("basic_mace")).isTrue();
        assertThat(inventory.contains("basic_light_chest")).isFalse();

        inventory.forceSetItemAt(0, chest);

        assertThat(inventory.contains("basic_mace")).isFalse();
        assertThat(inventory.contains("basic_light_chest")).isTrue();
    }

    @Test
    void whenSameResourceItemIsAdded_ShouldIncreaseAmount() {
        InventoryItem gold = InventoryDatabase.getInstance().getInventoryItem("gold");
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(1);
        inventory.autoSetItem(gold);
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(2);
    }

    @Test
    void whenSameEquipmentItemIsAdded_ShouldNotIncreaseAmount() {
        InventoryItem basic_mace = InventoryDatabase.getInstance().getInventoryItem("basic_mace");
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(0);
        inventory.autoSetItem(basic_mace);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
    }

    @Test
    void whenSameResourceItemOfMultipleIsAdded_ShouldIncreaseTheFirstAmount() {
        InventoryItem gold1 = InventoryDatabase.getInstance().getInventoryItem("gold");
        InventoryItem gold2 = InventoryDatabase.getInstance().getInventoryItem("gold");
        InventoryItem gold3 = InventoryDatabase.getInstance().getInventoryItem("gold");
        inventory.forceSetItemAt(0, gold1);
        inventory.forceSetItemAt(1, gold2);
        inventory.forceSetItemAt(2, gold3);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(2)).isEqualTo(1);

        InventoryItem moreGold = InventoryDatabase.getInstance().getInventoryItem("gold");
        inventory.autoSetItem(moreGold);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(2);
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getAmountOfItemAt(2)).isEqualTo(1);
    }

    @Test
    void whenInventoryIsFull_ShouldNotThrowError() {
        InventoryItem gold = InventoryDatabase.getInstance().getInventoryItem("gold");
        InventoryItem herbs = InventoryDatabase.getInstance().getInventoryItem("herbs");
        IntStream.range(0, inventory.getSize())
                 .forEach(i -> inventory.forceSetItemAt(i, gold));
        inventory.autoSetItem(herbs);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void whenInventoryItemWeapon_ShouldCreateDescription() {
        InventoryItem weapon = InventoryDatabase.getInstance().getInventoryItem("basic_mace");
        List<Map.Entry<String, String>> description = weapon.createDescription();
        assertThat(description.get(0).getKey()).isEqualTo("Weapon");
        assertThat(description.get(0).getValue()).isEqualTo("Basic Mace");
        assertThat(description.get(1).getKey()).isEqualTo("Skill");
        assertThat(description.get(1).getValue()).isEqualTo("Hafted");
        assertThat(description.get(2).getKey()).isEqualTo("Min. Strength");
        assertThat(description.get(2).getValue()).isEqualTo("15");
        assertThat(description.get(3).getKey()).isEqualTo("Base Hit");
        assertThat(description.get(3).getValue()).isEqualTo("30%");
        assertThat(description.get(4).getKey()).isEqualTo("Damage");
        assertThat(description.get(4).getValue()).isEqualTo("18");
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> description.get(5));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void whenInventoryItemShield_ShouldCreateDescription() {
        InventoryItem weapon = InventoryDatabase.getInstance().getInventoryItem("basic_light_shield");
        List<Map.Entry<String, String>> description = weapon.createDescription();
        assertThat(description.get(0).getKey()).isEqualTo("Shield");
        assertThat(description.get(0).getValue()).isEqualTo("Basic Light Shield");
        assertThat(description.get(1).getKey()).isEqualTo("Min. Strength");
        assertThat(description.get(1).getValue()).isEqualTo("14");
        assertThat(description.get(2).getKey()).isEqualTo("Protection");
        assertThat(description.get(2).getValue()).isEqualTo("1");
        assertThat(description.get(3).getKey()).isEqualTo("Defense");
        assertThat(description.get(3).getValue()).isEqualTo("5");
        assertThat(description.get(4).getKey()).isEqualTo("Dexterity");
        assertThat(description.get(4).getValue()).isEqualTo("-2");
        assertThat(description.get(5).getKey()).isEqualTo("Stealth");
        assertThat(description.get(5).getValue()).isEqualTo("-5");
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> description.get(6));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void whenInventoryItemChest_ShouldCreateDescription() {
        InventoryItem weapon = InventoryDatabase.getInstance().getInventoryItem("basic_light_chest");
        List<Map.Entry<String, String>> description = weapon.createDescription();
        assertThat(description.get(0).getKey()).isEqualTo("Chest");
        assertThat(description.get(0).getValue()).isEqualTo("Basic Light Chest");
        assertThat(description.get(1).getKey()).isEqualTo("Weight");
        assertThat(description.get(1).getValue()).isEqualTo("1");
        assertThat(description.get(2).getKey()).isEqualTo("Protection");
        assertThat(description.get(2).getValue()).isEqualTo("1");
        assertThat(description.get(3).getKey()).isEqualTo("Stealth");
        assertThat(description.get(3).getValue()).isEqualTo("0");
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> description.get(4));
    }

}
