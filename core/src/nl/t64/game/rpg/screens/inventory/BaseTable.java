package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import nl.t64.game.rpg.Utils;


public abstract class BaseTable {

    private static final String TEXT_FONT = "fonts/spectral_extra_bold_20.ttf";
    private static final int TEXT_SIZE = 20;
    private static final float LINE_HEIGHT = 26f;
    private static final float PADDING = 20f;
    private static final float PADDING_RIGHT = 10f;

    public final Table table;
    private final BitmapFont font;

    protected BaseTable() {
        this.font = Utils.getResourceManager().getTrueTypeAsset(TEXT_FONT, TEXT_SIZE);
        this.table = new Table(createSkin());
        this.table.defaults().height(LINE_HEIGHT);
        this.table.pad(PADDING).padRight(PADDING_RIGHT);
    }

    protected abstract void update();

    void addToTable(int totalExtra) {
        if (totalExtra > 0) {
            var label = new Label(String.format("+%s", totalExtra), new Label.LabelStyle(font, Color.FOREST));
            table.add(label).row();
        } else if (totalExtra < 0) {
            var label = new Label(String.valueOf(totalExtra), new Label.LabelStyle(font, Color.FIREBRICK));
            table.add(label).row();
        } else {
            table.add("").row();
        }
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
