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
    private static final float SHOW_DURATION = 10f;

    public MessageTooltip() {
        super.window.padLeft(PAD);
        super.window.padRight(PAD);
    }

    public void show(String message, Stage stage) {
        if (window.hasActions()) {
            window.addAction(Actions.sequence(Actions.fadeOut(Constant.FADE_DURATION),
                                              Actions.run(() -> setupAndShowWindow(message, stage))));
        } else {
            setupAndShowWindow(message, stage);
        }
    }

    private void setupAndShowWindow(String message, Stage stage) {
        setupWindow(message);
        showWindow(stage);
    }

    private void setupWindow(String message) {
        window.clear();
        final var labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        final var label = new Label(message, labelStyle);
        label.setAlignment(Align.center);
        window.add(label);
        window.pack();
        setPosition(Gdx.graphics.getWidth() - window.getWidth() - PAD,
                    Gdx.graphics.getHeight() - window.getHeight() - PAD);
    }

    private void showWindow(Stage stage) {
        window.setVisible(true);
        stage.addActor(window);
        window.addAction(Actions.sequence(Actions.alpha(0f),
                                          Actions.delay(DELAY),
                                          Actions.fadeIn(Constant.FADE_DURATION),
                                          Actions.delay(SHOW_DURATION),
                                          Actions.fadeOut(Constant.FADE_DURATION),
                                          Actions.removeActor()));
    }

}
