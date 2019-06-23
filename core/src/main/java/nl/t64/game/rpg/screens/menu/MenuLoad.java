package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;


public class MenuLoad extends MenuScreen {

    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";

    private static final String TITLE_LABEL = "Select your profile:";
    private static final String MENU_ITEM_LOAD = "Load";
    private static final String MENU_ITEM_DELETE = "Delete";
    private static final String MENU_ITEM_BACK = "Back";
    private static final String LOAD_MESSAGE = "Any unsaved progress will be lost.\nAre you sure?";
    private static final String DELETE_MESSAGE = "This save file will be removed.\nAre you sure?";

    private static final float TITLE_SPACE_BOTTOM = 30f;
    private static final float TOP_TABLE_Y = 50f;
    private static final float SCROLL_PANE_HEIGHT = 380f;
    private static final float BUTTON_SPACE_RIGHT = 150f;

    private static final int NUMBER_OF_ITEMS = 3;
    private static final int DELETE_INDEX = 1;
    private static final int EXIT_INDEX = 2;

    private ScreenType fromScreen;

    private Array<String> profiles;

    private Table topTable;
    private List listItems;
    private ScrollPane scrollPane;
    private Table bottomTable;
    private TextButton loadButton;
    private TextButton deleteButton;
    private TextButton backButton;
    private DialogQuestion progressLostDialog;
    private DialogQuestion deleteFileDialog;

    private ListenerKeyVertical listenerKeyVertical;
    private ListenerKeyHorizontal listenerKeyHorizontal;

    private int selectedListIndex;
    private int selectedMenuIndex;

    private boolean isMouseScrolled = false;

    public MenuLoad() {
        super();
    }

    @Override
    void setFromScreen(ScreenType screenType) {
        this.fromScreen = screenType;
    }

    @Override
    void setupScreen() {
        Utils.getProfileManager().loadAllProfiles();
        profiles = Utils.getProfileManager().getProfileList();

        createTables();
        progressLostDialog = new DialogQuestion(this::openWorldScreen, LOAD_MESSAGE);
        deleteFileDialog = new DialogQuestion(this::deleteSaveFile, DELETE_MESSAGE);
        applyListeners();

        stage.addActor(topTable);
        stage.addActor(bottomTable);
        stage.setKeyboardFocus(scrollPane);
        stage.setScrollFocus(scrollPane);

        selectedListIndex = 0;
        selectedMenuIndex = 0;
        setCurrentTextButtonToRed();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        selectedListIndex = listItems.getSelectedIndex();
        scrollScrollPane();
        listenerKeyVertical.updateSelectedIndex(selectedListIndex);
        listenerKeyHorizontal.updateSelectedIndex(selectedMenuIndex);
        progressLostDialog.render(dt); // for updating the index in de listener.
        deleteFileDialog.render(dt);
        stage.draw();
    }

    private void scrollScrollPane() {
        if (!isMouseScrolled) {
            float itemHeight = listItems.getItemHeight();
            float listHeight = itemHeight * listItems.getItems().size;
            float selectedY = listHeight - (itemHeight * (selectedListIndex + 1f));
            scrollPane.scrollTo(0, selectedY, 0, 0, false, true);
        }
    }

    private void busyScrolling() {
        isMouseScrolled = true;
    }

    private void updateListIndex(Integer newIndex) {
        isMouseScrolled = false;
        listItems.setSelectedIndex(newIndex);
    }

    private void updateMenuIndex(Integer newIndex) {
        selectedMenuIndex = newIndex;
        setAllTextButtonsToWhite();
        setCurrentTextButtonToRed();
    }

    private void selectMenuItem() {
        switch (selectedMenuIndex) {
            case 0:
                processLoadButton();
                break;
            case 1:
                processDeleteButton();
                break;
            case 2:
                processBackButton();
                break;
            default:
                throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processLoadButton() {
        Object profileName = listItems.getSelected();
        if (profileName != null) {
            if (fromScreen.equals(ScreenType.MENU_PAUSE)) {
                progressLostDialog.show(stage);
            } else {
                openWorldScreen();
            }
        }
    }

    private void processDeleteButton() {
        Object profileName = listItems.getSelected();
        if (profileName != null) {
            deleteFileDialog.show(stage);
        }
    }

    private void processBackButton() {
        if (fromScreen.equals(ScreenType.MENU_PAUSE)) {
            var menuPause = Utils.getScreenManager().getMenuScreen(fromScreen);
            menuPause.setBackground(screenshot);
        }
        Utils.getScreenManager().setScreen(fromScreen);
        fromScreen = null;
    }

    private void openWorldScreen() {
        Object profileName = listItems.getSelected();
        Utils.getScreenManager().setScreen(ScreenType.WORLD);
        Utils.getProfileManager().loadProfile(profileName.toString());
    }

    private void deleteSaveFile() {
        int selectedIndex = listItems.getSelectedIndex();
        if (selectedIndex == profiles.size - 1) {
            selectedIndex -= 1;
        }
        Object profileName = listItems.getSelected();
        profiles = Utils.getProfileManager().removeProfile(profileName.toString());
        listItems.setItems(profiles);
        scrollPane.clearChildren();
        scrollPane.setActor(listItems);
        updateListIndex(selectedIndex);
        listenerKeyVertical.updateNumberOfItems(listItems.getItems().size);
    }

    private void setAllTextButtonsToWhite() {
        for (Actor actor : bottomTable.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = Color.WHITE;
        }
    }

    private void setCurrentTextButtonToRed() {
        ((TextButton) bottomTable.getChildren().get(selectedMenuIndex)).getStyle().fontColor = Constant.DARK_RED;
    }

    private void createTables() {
        var spriteTransparent = new Sprite(Utils.getResourceManager().getTextureAsset(SPRITE_TRANSPARENT));
        // styles
        var titleStyle = new Label.LabelStyle(menuFont, Color.WHITE);
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;
        var listStyle = new List.ListStyle();
        listStyle.font = menuFont;
        listStyle.fontColorSelected = Constant.DARK_RED;
        listStyle.fontColorUnselected = Color.WHITE;
        listStyle.background = new SpriteDrawable(spriteTransparent);
        listStyle.selection = new SpriteDrawable(spriteTransparent);

        // actors
        var titleLabel = new Label(TITLE_LABEL, titleStyle);
        loadButton = new TextButton(MENU_ITEM_LOAD, new TextButton.TextButtonStyle(buttonStyle));
        deleteButton = new TextButton(MENU_ITEM_DELETE, new TextButton.TextButtonStyle(buttonStyle));
        backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        listItems = new List(listStyle);
        listItems.setItems(profiles);
        scrollPane = new ScrollPane(listItems);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);

        // tables
        topTable = new Table();
        topTable.setFillParent(true);
        topTable.setY(TOP_TABLE_Y);
        topTable.add(titleLabel).spaceBottom(TITLE_SPACE_BOTTOM).row();
        topTable.add(scrollPane);
        topTable.getCell(scrollPane).height(SCROLL_PANE_HEIGHT);

        bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.setY(-(TOP_TABLE_Y + (SCROLL_PANE_HEIGHT / 2f)));
        bottomTable.add(loadButton).spaceRight(BUTTON_SPACE_RIGHT);
        bottomTable.add(deleteButton).spaceRight(BUTTON_SPACE_RIGHT);
        bottomTable.add(backButton);
    }

    private void applyListeners() {
        listenerKeyVertical = new ListenerKeyVertical(this::updateListIndex, listItems.getItems().size);
        listenerKeyHorizontal = new ListenerKeyHorizontal(this::updateMenuIndex, NUMBER_OF_ITEMS);
        scrollPane.addListener(listenerKeyVertical);
        scrollPane.addListener(listenerKeyHorizontal);
        scrollPane.addListener(new ListenerKeyConfirm(this::updateMenuIndex, this::selectMenuItem, EXIT_INDEX));
        scrollPane.addListener(new ListenerKeyDelete(this::updateMenuIndex, this::selectMenuItem, DELETE_INDEX));
        scrollPane.addListener(new ListenerMouseScrollPane(this::busyScrolling));
        loadButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 0));
        deleteButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 1));
        backButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 2));
    }

}
