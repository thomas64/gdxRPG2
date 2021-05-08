package nl.t64.game.rpg.sfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class TransitionImage extends Image {

    private static final float TEN_TIMES_FULL_HD = 10f;

    public TransitionImage() {
        this(Color.BLACK);
    }

    public TransitionImage(Color color) {
        super.toFront();
        super.setFillParent(true);

        var pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        super.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(pixmap))));
        pixmap.dispose();
        super.clearListeners();
        super.setTouchable(Touchable.disabled);
        super.scaleBy(TEN_TIMES_FULL_HD);
    }

}
