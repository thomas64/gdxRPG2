package nl.t64.game.rpg.components.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.menu.InitMenuEvent;
import nl.t64.game.rpg.events.menu.UpdateIndexEvent;


public class MainMenuGraphicsComponent extends GraphicsComponent {

    private static final Color DARK_RED = new Color(0.5f, 0f, 0f, 1f);

    private static final String TITLE_FONT = "fonts/colonna.ttf";
    private static final int TITLE_SIZE = 200;
    private static final int TITLE_SPACE_BOTTOM = 75;

    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;
    private static final int MENU_SPACE_BOTTOM = 10;

    private static final String TITLE = "gdxRPG2";

    private static final String MENU_ITEM_NEW_GAME = "New Game";
    private static final String MENU_ITEM_LOAD_GAME = "Load Game";
    private static final String MENU_ITEM_SETTINGS = "Settings";
    private static final String MENU_ITEM_EXIT = "Exit";

    private Table table;
    private int selectedIndex;

    public MainMenuGraphicsComponent() {
        Label titleLabel = createTitleLabel();
        this.table = createTable(titleLabel);
    }

    @Override
    public void receive(Event event) {
        if (event instanceof InitMenuEvent) {
            InitMenuEvent initMenuEvent = (InitMenuEvent) event;
            selectedIndex = initMenuEvent.getSelectedIndex();
            initMenuEvent.getStage().addActor(table);
        }
        if (event instanceof UpdateIndexEvent) {
            selectedIndex = ((UpdateIndexEvent) event).getSelectedIndex();
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update() {
        setAllTextButtonsToWhite();
        setCurrentTextButtonToRed();
    }

    private Label createTitleLabel() {
        BitmapFont titleFont = Utility.generateBitmapFontFromFreeTypeFont(TITLE_FONT, TITLE_SIZE);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, DARK_RED);
        return new Label(TITLE, titleStyle);
    }

    private Table createTable(Label titleLabel) {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = Utility.generateBitmapFontFromFreeTypeFont(MENU_FONT, MENU_SIZE);
        buttonStyle.fontColor = Color.WHITE;

        TextButton newGameButton = new TextButton(MENU_ITEM_NEW_GAME, new TextButton.TextButtonStyle(buttonStyle));
        TextButton loadGameButton = new TextButton(MENU_ITEM_LOAD_GAME, new TextButton.TextButtonStyle(buttonStyle));
        TextButton settingsButton = new TextButton(MENU_ITEM_SETTINGS, new TextButton.TextButtonStyle(buttonStyle));
        TextButton exitButton = new TextButton(MENU_ITEM_EXIT, new TextButton.TextButtonStyle(buttonStyle));

        Table newTable = new Table();
        newTable.setFillParent(true);
        newTable.add(titleLabel).spaceBottom(TITLE_SPACE_BOTTOM).row();
        newTable.add(newGameButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(loadGameButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(settingsButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(exitButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        return newTable;
    }

    private void setAllTextButtonsToWhite() {
        for (Actor actor : table.getChildren()) {
            if (actor instanceof TextButton) {
                ((TextButton) actor).getStyle().fontColor = Color.WHITE;
            }
        }
    }

    private void setCurrentTextButtonToRed() {
        selectedIndex += 1; // because the title is also in the table.
        ((TextButton) table.getChildren().get(selectedIndex)).getStyle().fontColor = DARK_RED;
        selectedIndex -= 1;
    }

}
