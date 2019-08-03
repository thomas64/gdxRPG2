package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;


abstract class BaseToolTip {

    private static final String SPRITE_SLOT = "sprites/tooltip.png";
    private static final float PADDING = 10f;

    final BitmapFont font;
    final Window window;

    BaseToolTip() {
        this.font = new BitmapFont();
        this.window = new Window("", createWindowStyle());
        this.window.defaults().align(Align.left);
        this.window.pad(PADDING);
        this.window.pack();
        this.window.setVisible(false);
    }

    private Window.WindowStyle createWindowStyle() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_SLOT);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        var drawable = new NinePatchDrawable(ninepatch);
        return new Window.WindowStyle(font, Color.GREEN, drawable);
    }

    void addToStage(Stage stage) {
        stage.addActor(window);
    }

    void setPosition(float x, float y) {
        window.setPosition(x, y);
    }

    void toFront() {
        window.toFront();
    }

}
