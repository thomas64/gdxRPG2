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
    private static final String TITLE = "gdxRPG2";
    private static final String TITLE_FONT = "fonts/colonna.ttf";
    private static final int TITLE_SIZE = 200;
    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;

    private Table table;
    private int selectedIndex;

    public MainMenuGraphicsComponent() {
        this.table = new Table();
        this.table.setFillParent(true);
        this.fillTable();
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
    }

    @Override
    public void update() {
        setAllTextButtonsToWhite();
        setCurrentTextButtonToRed();
    }

    private void fillTable() {
        Label titleLabel = createTitleLabel();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = Utility.generateBitmapFontFromFreeTypeFont(MENU_FONT, MENU_SIZE);
        buttonStyle.fontColor = Color.WHITE;

        TextButton newGameButton = new TextButton("New Game", new TextButton.TextButtonStyle(buttonStyle));
        TextButton loadGameButton = new TextButton("Load Game", new TextButton.TextButtonStyle(buttonStyle));
        TextButton settingsButton = new TextButton("Settings", new TextButton.TextButtonStyle(buttonStyle));
        TextButton exitButton = new TextButton("Exit", new TextButton.TextButtonStyle(buttonStyle));

        table.add(titleLabel).spaceBottom(75).row();
        table.add(newGameButton).spaceBottom(10).row();
        table.add(loadGameButton).spaceBottom(10).row();
        table.add(settingsButton).spaceBottom(10).row();
        table.add(exitButton).spaceBottom(10).row();
    }

    private Label createTitleLabel() {
        BitmapFont titleFont = Utility.generateBitmapFontFromFreeTypeFont(TITLE_FONT, TITLE_SIZE);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, DARK_RED);
        return new Label(TITLE, titleStyle);
    }

    private void setAllTextButtonsToWhite() {
        for (Actor actor : table.getChildren()) {
            if (actor instanceof TextButton) {
                ((TextButton) actor).getStyle().fontColor = Color.WHITE;
            }
        }
    }

    private void setCurrentTextButtonToRed() {
        ((TextButton) table.getChildren().get(selectedIndex)).getStyle().fontColor = DARK_RED;
    }

//    private Skin createMenuSkin() {
//        BitmapFont menuFont = Utility.generateBitmapFontFromFreeTypeFont(MENU_FONT, MENU_SIZE);
//        Skin skin = new Skin();
//        skin.add("default-font", menuFont, BitmapFont.class);
//        atlas = new TextureAtlas(UISKIN_ATLAS);
//        skin.addRegions(atlas);
//        skin.load(Gdx.files.internal(MENU_JSON));
//        return skin;
//    }

}
