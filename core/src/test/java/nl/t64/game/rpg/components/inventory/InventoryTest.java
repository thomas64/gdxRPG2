package nl.t64.game.rpg.components.inventory;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


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

        assertThat(inventory.contains("gold")).isTrue();
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        assertThat(inventory.getItemAt(0)).get().hasFieldOrPropertyWithValue("id", "gold");

        assertThat(inventory.contains("basic_mace")).isTrue();
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex())).isEqualTo(1);
        assertThat(inventory.getItemAt(inventory.getLastIndex()))
                .get().hasFieldOrPropertyWithValue("id", "basic_mace");
    }

    @Test
    void whenResourceItemIsAdded_ShouldBeAddedToStartOfInventory() {
        InventoryItem herbs = InventoryDatabase.getInstance().getInventoryItem("herbs");
        assertThat(inventory.contains("herbs")).isFalse();
        inventory.autoSetItem(herbs);
        assertThat(inventory.contains("herbs")).isTrue();
        assertThat(inventory.getAmountOfItemAt(1)).isEqualTo(1);
        assertThat(inventory.getItemAt(1)).get().hasFieldOrPropertyWithValue("id", "herbs");
    }

    @Test
    void whenEquipmentItemIsAdded_ShouldBeAddedToEndOfInventory() {
        InventoryItem chest = InventoryDatabase.getInstance().getInventoryItem("light_basic_chest");
        assertThat(inventory.contains("light_basic_chest")).isFalse();
        inventory.autoSetItem(chest);
        assertThat(inventory.contains("light_basic_chest")).isTrue();
        assertThat(inventory.getAmountOfItemAt(inventory.getLastIndex() - 1)).isEqualTo(1);
        assertThat(inventory.getItemAt(inventory.getLastIndex() - 1))
                .get().hasFieldOrPropertyWithValue("id", "light_basic_chest");
    }

    @Test
    void whenItemIsForceSet_ShouldOverwriteExistingItem() {
        InventoryItem chest = InventoryDatabase.getInstance().getInventoryItem("light_basic_chest");
        assertThat(inventory.contains("basic_mace")).isTrue();
        assertThat(inventory.contains("light_basic_chest")).isFalse();

        inventory.forceSetItemAt(inventory.getLastIndex(), chest);

        assertThat(inventory.contains("basic_mace")).isFalse();
        assertThat(inventory.contains("light_basic_chest")).isTrue();
    }

    @Test
    void whenSameResourceItemIsAdded_ShouldIncreaseAmount() {
        InventoryItem gold = InventoryDatabase.getInstance().getInventoryItem("gold");
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        inventory.autoSetItem(gold);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(2);
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

}
