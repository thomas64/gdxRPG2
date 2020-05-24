package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.LoadScreen;


public class ShopLoadScreen extends LoadScreen {

    @Override
    protected void setScreen() {
        var shopScreen = (ShopScreen) Utils.getScreenManager().getScreen(ScreenType.SHOP);
        shopScreen.setBackground((Image) stage.getActors().get(0),
                                 (Image) stage.getActors().get(1));
        Utils.getScreenManager().setScreen(ScreenType.SHOP);
    }

}
