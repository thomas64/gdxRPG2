package nl.t64.game.rpg.components.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.menu.InitMenuEvent;
import nl.t64.game.rpg.events.menu.UpdateIndexEvent;
import nl.t64.game.rpg.events.menu.UpdateProfileNameEvent;


public class NewGameMenuGraphicsComponent extends GraphicsComponent {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;

    private static final String PROFILE_NAME = "Enter Profile Name:";
    private static final int PROFILE_TEXT_LENGTH = 8 + 1;

    private static final String MENU_ITEM_START = "Start";
    private static final String MENU_ITEM_BACK = "Back";

    private static final int PROFILE_NAME_SPACE_RIGHT = 20;
    private static final int PROFILE_TEXT_WIDTH = 280;
    private static final int PROFILE_TEXT_HEIGHT = 75;
    private static final int START_BUTTON_SPACE_RIGHT = 150;
    private static final int UPPER_TABLE_SPACE_BOTTOM = 50;

    private BitmapFont menuFont;
    private TextField profileText;
    private Table table;
    private int selectedIndex;

    public NewGameMenuGraphicsComponent() {
        this.menuFont = Utility.generateBitmapFontFromFreeTypeFont(MENU_FONT, MENU_SIZE);
        this.table = createTable();
    }

    @Override
    public void receive(Event event) {
        if (event instanceof InitMenuEvent) {
            InitMenuEvent initMenuEvent = (InitMenuEvent) event;
            selectedIndex = initMenuEvent.getSelectedIndex();
            initMenuEvent.getStage().addActor(table);
        }
        if (event instanceof UpdateProfileNameEvent) {
            profileText.setText(((UpdateProfileNameEvent) event).getProfileName());
        }
        if (event instanceof UpdateIndexEvent) {
            selectedIndex = ((UpdateIndexEvent) event).getSelectedIndex();
        }
    }

    @Override
    public void dispose() {
        menuFont.dispose();
    }

    @Override
    public void update() {
        setAllTextButtonsToWhite();
        setCurrentTextButtonToRed();
    }

    private Table createTable() {
//        Skin uiskin = new Skin();
//        uiskin.add("default-font", menuFont, BitmapFont.class);
//        TextureAtlas atlas = new TextureAtlas(UISKIN_ATLAS);
//        uiskin.addRegions(atlas);
//        uiskin.load(Gdx.files.internal(UISKIN_JSON));

        Label.LabelStyle menuStyle = new Label.LabelStyle(menuFont, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;

        Label profileName = new Label(PROFILE_NAME, menuStyle);
        profileText = createProfileText();

        TextButton startButton = new TextButton(MENU_ITEM_START, new TextButton.TextButtonStyle(buttonStyle));
        TextButton backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        Table newTable = new Table();
        newTable.setFillParent(true);

        Table upperTable = new Table();
        upperTable.add(profileName).spaceRight(PROFILE_NAME_SPACE_RIGHT);
        upperTable.add(profileText).width(PROFILE_TEXT_WIDTH).height(PROFILE_TEXT_HEIGHT);

        Table lowerTable = new Table();
        lowerTable.add(startButton).spaceRight(START_BUTTON_SPACE_RIGHT);
        lowerTable.add(backButton);

        newTable.add(upperTable).spaceBottom(UPPER_TABLE_SPACE_BOTTOM).row();
        newTable.add(lowerTable);
        return newTable;
    }

    private TextField createProfileText() {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = menuFont;
        textFieldStyle.fontColor = Color.BLACK;
        Utility.loadTextureAsset(SPRITE_PARCHMENT);
        Texture texture = Utility.getTextureAsset(SPRITE_PARCHMENT);
        Sprite sprite = new Sprite(texture);
        textFieldStyle.background = new SpriteDrawable(sprite);

        TextField textField = new TextField("", textFieldStyle);
        textField.setMaxLength(PROFILE_TEXT_LENGTH);
        textField.setAlignment(Align.center);
        return textField;
    }

    private void setAllTextButtonsToWhite() {
        Table lowerTable = (Table) table.getChildren().get(1); // two tables inside the table, get the second.
        for (Actor actor : lowerTable.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = Color.WHITE;
        }
    }

    private void setCurrentTextButtonToRed() {
        Table lowerTable = (Table) table.getChildren().get(1); // two tables inside the table, get the second.
        ((TextButton) lowerTable.getChildren().get(selectedIndex)).getStyle().fontColor = Constant.DARK_RED;
    }

}
