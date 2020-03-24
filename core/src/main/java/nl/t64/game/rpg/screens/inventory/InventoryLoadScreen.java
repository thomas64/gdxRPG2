package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.LoadScreen;


public class InventoryLoadScreen extends LoadScreen {

    @Override
    protected void setScreen() {
        var inventoryScreen = (InventoryScreen) Utils.getScreenManager().getScreen(ScreenType.INVENTORY);
        inventoryScreen.setBackground((Image) stage.getActors().get(0));
        Utils.getScreenManager().setScreen(ScreenType.INVENTORY);
    }

}
