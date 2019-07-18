package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Scaling;
import nl.t64.game.rpg.Utils;


abstract class BaseTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final String TEXT_FONT = "fonts/spectral.ttf";
    private static final int TEXT_SIZE = 20;
    private static final float LINE_HEIGHT = 26f;
    private static final float PADDING = 20f;
    private static final float PADDING_RIGHT = 10f;

    final Table table;
    private final BitmapFont font;

    BaseTable() {
        this.font = Utils.getResourceManager().getTrueTypeAsset(TEXT_FONT, TEXT_SIZE);
        this.table = new Table(createSkin());
        this.table.defaults().height(LINE_HEIGHT);
        this.table.pad(PADDING).padRight(PADDING_RIGHT);
    }

    abstract void render();

    void createBonusFromInventory(int totalFromInventory) {
        if (totalFromInventory > 0) {
            var label = new Label(String.format("+%s", totalFromInventory), new Label.LabelStyle(font, Color.FOREST));
            table.add(label);
        } else if (totalFromInventory < 0) {
            var label = new Label(String.valueOf(totalFromInventory), new Label.LabelStyle(font, Color.FIREBRICK));
            table.add(label);
        } else {
            table.add("");
        }
    }

    void createPreview(int difference) {
        if (difference > 0) {
            var label = new Label(String.format("+%s", difference), new Label.LabelStyle(font, Color.LIME));
            table.add(label).row();
        } else if (difference < 0) {
            var label = new Label(String.valueOf(difference), new Label.LabelStyle(font, Color.SCARLET));
            table.add(label).row();
        } else {
            table.add("").row();
        }
    }

    void setTopBorder(Table table) {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        table.setBackground(drawable);
    }

    Image createImageOf(String id) {
        var textureRegion = Utils.getResourceManager().getAtlasTexture(id.toLowerCase());
        var image = new Image(textureRegion);
        image.setScaling(Scaling.none);
        return image;
    }

    private Skin createSkin() {
        Skin skin = new Skin();
        skin.add("default", new Label.LabelStyle(font, Color.BLACK));
        return skin;
    }

}