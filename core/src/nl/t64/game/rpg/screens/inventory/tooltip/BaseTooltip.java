package nl.t64.game.rpg.screens.inventory.tooltip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;


public abstract class BaseTooltip {

    private static final String SPRITE_SLOT = "sprites/tooltip.png";
    private static final float PADDING = 10f;

    final Window window;

    BaseTooltip() {
        this.window = new Window("", createWindowStyle());
        this.window.defaults().align(Align.left);
        this.window.pad(PADDING);
        this.window.pack();
        this.window.setVisible(false);
    }

    private static Window.WindowStyle createWindowStyle() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_SLOT);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        var drawable = new NinePatchDrawable(ninepatch);
        return new Window.WindowStyle(new BitmapFont(), Color.GREEN, drawable);
    }

    public void toggle(ItemSlot itemSlot) {
        throw new IllegalCallerException("Implement this method in child.");
    }

    public void toggleCompare(ItemSlot itemSlot) {
        // do nothing. some tooltips don't need compare.
    }

    public void addToStage(Stage stage) {
        stage.addActor(window);
    }

    public void hide() {
        window.clearActions();
        window.setVisible(false);
    }

}
