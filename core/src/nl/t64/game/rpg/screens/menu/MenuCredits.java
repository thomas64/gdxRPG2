package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;


public class MenuCredits extends MenuScreen {

    private static final String CREDITS_PATH = "licenses/credits.txt";
    private static final int SCROLL_SPEED = 100;
    private static final int EMPTY_LINES = 20;

    private final ScrollPane scrollPane;

    public MenuCredits() {
        String emptyLines = System.lineSeparator().repeat(EMPTY_LINES);
        String creditsText = emptyLines + Gdx.files.internal(CREDITS_PATH).readString() + emptyLines;
        var textStyle = new Label.LabelStyle(menuFont, Color.BLACK);
        var credits = new Label(creditsText, textStyle);
        credits.setAlignment(Align.top | Align.center);
        credits.setWrap(true);
        this.scrollPane = new ScrollPane(credits);
        this.createTable();
    }

    @Override
    void setupScreen() {
        scrollPane.setScrollY(0);
        scrollPane.updateVisualScroll();
        applyListeners();
        stage.addActor(table);
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(dt);
        scrollPane.setScrollY(scrollPane.getScrollY() + dt * SCROLL_SPEED);
        stage.draw();
    }

    private void createTable() {
        table = new Table();
        table.center();
        table.setFillParent(true);
        table.defaults().width(Gdx.graphics.getWidth());
        table.add(scrollPane);
    }

    private void applyListeners() {
        stage.addListener(new ListenerKeyConfirm(super::processBackButton));
        stage.addListener(new ListenerKeyCancel(super::processBackButton));
    }

}
