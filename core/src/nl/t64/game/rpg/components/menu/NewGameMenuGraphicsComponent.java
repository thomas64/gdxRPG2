package nl.t64.game.rpg.components.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.menu.InitMenuEvent;
import nl.t64.game.rpg.events.menu.UpdateIndexEvent;
import nl.t64.game.rpg.events.menu.UpdateProfileNameEvent;


public class NewGameMenuGraphicsComponent extends GraphicsComponent {

    private static final Color DARK_RED = new Color(0.5f, 0f, 0f, 1f);

    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;

    private static final String UISKIN_ATLAS = "skins/uiskin.atlas";
    private static final String UISKIN_JSON = "skins/uiskin.json";

    private static final String PROFILE_NAME = "Enter Profile Name:";
    private static final int PROFILE_TEXT_LENGTH = 9;

    private static final String MENU_ITEM_START = "Start";
    private static final String MENU_ITEM_BACK = "Back";

    private static final String DIALOG_TITLE = "Overwrite?";
    private static final String DIALOG_TEXT = "Overwrite existing profile name?";
    private static final String DIALOG_CANCEL = "Cancel";
    private static final String DIALOG_OVERWRITE = "Overwrite";

    private static final int PROFILE_NAME_SPACE_RIGHT = 20;
    private static final int PROFILE_TEXT_WIDTH = 200;
    private static final int START_BUTTON_SPACE_RIGHT = 150;
    private static final int UPPER_TABLE_SPACE_BOTTOM = 30;

    private Table table;
    private TextField profileText;
    private int selectedIndex;

    public NewGameMenuGraphicsComponent() {
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
        // empty
    }

    @Override
    public void update() {
        setAllTextButtonsToWhite();
        setCurrentTextButtonToRed();
    }

    private Table createTable() {
        BitmapFont menuFont = Utility.generateBitmapFontFromFreeTypeFont(MENU_FONT, MENU_SIZE);

        Skin uiskin = new Skin();
        uiskin.add("default-font", menuFont, BitmapFont.class);
        TextureAtlas atlas = new TextureAtlas(UISKIN_ATLAS);
        uiskin.addRegions(atlas);
        uiskin.load(Gdx.files.internal(UISKIN_JSON));

//        Label.LabelStyle menuStyle = new Label.LabelStyle(menuFont, Color.WHITE);
        Label profileName = new Label(PROFILE_NAME, uiskin);

//        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
//        textFieldStyle.font = menuFont;
//        textFieldStyle.fontColor = Color.WHITE;
        profileText = new TextField("", uiskin);
        profileText.setMaxLength(PROFILE_TEXT_LENGTH);

        Label overwriteLabel = new Label(DIALOG_TEXT, uiskin);

        Dialog.WindowStyle windowStyle = new Dialog.WindowStyle();
        windowStyle.titleFont = menuFont;
        windowStyle.titleFontColor = Color.WHITE;
        Dialog overwriteDialog = new Dialog(DIALOG_TITLE, windowStyle);
        overwriteDialog.setKeepWithinStage(true);
        overwriteDialog.setModal(true);
        overwriteDialog.setMovable(false);
        overwriteDialog.text(overwriteLabel);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;
        TextButton cancelButton = new TextButton(DIALOG_CANCEL, buttonStyle);
        TextButton overwriteButton = new TextButton(DIALOG_OVERWRITE, buttonStyle);
        TextButton startButton = new TextButton(MENU_ITEM_START, new TextButton.TextButtonStyle(buttonStyle));
        TextButton backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        //Layout
        overwriteDialog.row();
        overwriteDialog.button(overwriteButton).bottom().left();
        overwriteDialog.button(cancelButton).bottom().right();

        Table newTable = new Table();
        newTable.setFillParent(true);

        Table upperTable = new Table();
        upperTable.add(profileName).spaceRight(PROFILE_NAME_SPACE_RIGHT);
        upperTable.add(profileText).width(PROFILE_TEXT_WIDTH);

        Table lowerTable = new Table();
        lowerTable.add(startButton).spaceRight(START_BUTTON_SPACE_RIGHT);
        lowerTable.add(backButton);

        newTable.add(upperTable).spaceBottom(UPPER_TABLE_SPACE_BOTTOM).row();
        newTable.add(lowerTable);
        return newTable;
    }

    private void setAllTextButtonsToWhite() {
        Table lowerTable = (Table) table.getChildren().get(1); // two tables inside the table, get the second.
        for (Actor actor : lowerTable.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = Color.WHITE;
        }
    }

    private void setCurrentTextButtonToRed() {
        Table lowerTable = (Table) table.getChildren().get(1); // two tables inside the table, get the second.
        ((TextButton) lowerTable.getChildren().get(selectedIndex)).getStyle().fontColor = DARK_RED;
    }

}
