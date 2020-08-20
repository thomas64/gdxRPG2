package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.LoadScreen;


abstract class LootLoadScreen extends LoadScreen {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";

    @Override
    protected abstract void setScreen();

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
