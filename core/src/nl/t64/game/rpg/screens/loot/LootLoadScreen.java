package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.LoadScreen;


public class LootLoadScreen extends LoadScreen {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";

    @Override
    protected void setScreen() {
        var lootScreen = (LootScreen) Utils.getScreenManager().getScreen(ScreenType.LOOT);
        lootScreen.setBackground((Image) stage.getActors().get(0),
                                 (Image) stage.getActors().get(1));
        Utils.getScreenManager().setScreen(ScreenType.LOOT);
    }

    @Override
    public void setBackground(Image screenshot) {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_PARCHMENT);
        var parchment = new Image(texture);
        float width = Gdx.graphics.getWidth() / 2.4f;
        float height = Gdx.graphics.getHeight() / 3f;
        parchment.setSize(width, height);
        parchment.setPosition((Gdx.graphics.getWidth() / 2f) - (width / 2f),
                              (Gdx.graphics.getHeight() / 2f) - (height / 2f));
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

}
