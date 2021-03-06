package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;


public interface WindowSelector {

    void setKeyboardFocus(Stage stage);

    ItemSlot getCurrentSlot();

    void deselectCurrentSlot();

    void selectCurrentSlot();
}
