package nl.t64.game.rpg.components.tooltip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.constants.Constant;


public class MessageTooltip extends BaseTooltip {

    private static final float PAD = 50f;
    private static final float DELAY = 1f;
    private static final float SHOW_DURATION = 5f;

    private final Label label;

    public MessageTooltip() {
        super.window.padLeft(PAD);
        super.window.padRight(PAD);
        var labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        this.label = new Label(null, labelStyle);
        this.label.setAlignment(Align.center);
        super.window.add(this.label);
    }

    public void show(String message, Stage stage) {
        stage.addActor(window);
        showWindow(message);
    }

    private void showWindow(String message) {
        window.addAction(Actions.after(Actions.sequence(Actions.run(() -> setupWindow(message)),
                                                        Actions.alpha(0f),
                                                        Actions.visible(true),
                                                        Actions.delay(DELAY),
                                                        Actions.fadeIn(Constant.FADE_DURATION),
                                                        Actions.delay(SHOW_DURATION),
                                                        Actions.fadeOut(Constant.FADE_DURATION),
                                                        Actions.visible(false))));
    }

    private void setupWindow(String message) {
        label.setText(message);
        window.pack();
        setPosition(Gdx.graphics.getWidth() - window.getWidth() - PAD,
                    Gdx.graphics.getHeight() - window.getHeight() - PAD);
    }

}
