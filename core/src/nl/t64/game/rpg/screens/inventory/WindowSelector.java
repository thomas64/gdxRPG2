package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.tooltip.BaseTooltip;


public interface WindowSelector {

    void setKeyboardFocus(Stage stage);

    ItemSlot getCurrentSlot();

    BaseTooltip getCurrentTooltip();

    void deselectCurrentSlot();

    void selectCurrentSlot();

    void hideTooltip();

    default void takeOne() {
    }

    default void takeHalf() {
    }

    default void takeFull() {
    }

    default void doAction() {
    }

}
