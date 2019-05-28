package nl.t64.game.rpg.components.inventory;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class InventoryTest extends GameTest {

    private GameData gameData;
    private GlobalContainer inventory;

    @BeforeEach
    private void setup() {
        final var profileManager = new ProfileManager();
        gameData = new GameData();
        gameData.onNotifyCreate(profileManager);
        inventory = gameData.getInventory();
    }

    @Test
    void whenDataIsCreated_ShouldContainItems() {
        assertThat(inventory.getSize()).isEqualTo(2);
        assertThat(inventory.contains("gold")).isTrue();
        assertThat(inventory.contains("basic_mace")).isTrue();
    }

    @Test
    void whenEquipmentItemIsAdded_ShouldBeAddedToInventory() {
        InventoryItem chest = InventoryDatabase.getInstance().getInventoryItem("light_basic_chest");
        assertThat(inventory.contains("light_basic_chest")).isFalse();
        inventory.setItemAt(2, chest);
        assertThat(inventory.contains("light_basic_chest")).isTrue();
    }

    @Test
    void whenEquipmentItemIsAddedTwice_ShouldThrowException() {
        InventoryItem chest = InventoryDatabase.getInstance().getInventoryItem("light_basic_chest");
        inventory.setItemAt(2, chest);
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(
                () -> inventory.setItemAt(2, chest)
        );
    }

    @Test
    void whenResourceItemIsAdded_ShouldBeAddedToInventory() {
        InventoryItem gold = InventoryDatabase.getInstance().getInventoryItem("gold");
        assertThat(inventory.getAmountOfItemAt(2)).isEqualTo(0);
        inventory.setItemAt(2, gold);
        assertThat(inventory.getAmountOfItemAt(2)).isEqualTo(1);
    }

    @Test
    void whenResourceItemIsAddedTwice_ShouldBeAddedToInventory() {
        InventoryItem gold = InventoryDatabase.getInstance().getInventoryItem("gold");
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(1);
        inventory.setItemAt(0, gold);
        assertThat(inventory.getAmountOfItemAt(0)).isEqualTo(2);
    }

    @Test
    void whenOtherResourceItemIsAddedOnSameSpot_ShouldThrowException() {
        InventoryItem herbs = InventoryDatabase.getInstance().getInventoryItem("herbs");
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(
                () -> inventory.setItemAt(0, herbs)
        );
    }

}