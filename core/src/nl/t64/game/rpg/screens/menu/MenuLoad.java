package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;


public class MenuLoad extends MenuScreen {

    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";

    private static final String TITLE_LABEL = "Select your profile:";
    private static final String MENU_ITEM_LOAD = "Load";
    private static final String MENU_ITEM_DELETE = "Delete";
    private static final String MENU_ITEM_BACK = "Back";
    private static final String LOAD_MESSAGE = "All progress after the last save will be lost."
                                               + System.lineSeparator() + "Are you sure you want to load this profile?";
    private static final String DELETE_MESSAGE = "This save file will be removed."
                                                 + System.lineSeparator() + "Are you sure?";

    private static final float TITLE_SPACE_BOTTOM = 10f;
    private static final float SCROLL_PANE_HEIGHT = 220f;
    private static final float BUTTON_SPACE_RIGHT = 20f;

    private static final int NUMBER_OF_ITEMS = 3;
    private static final int DELETE_INDEX = 1;
    private static final int EXIT_INDEX = 2;

    private Array<String> profiles;

    private Table topTable;
    private List<String> listItems;
    private ScrollPane scrollPane;
    private TextButton loadButton;
    private TextButton deleteButton;
    private TextButton backButton;
    private DialogQuestion progressLostDialog;
    private DialogQuestion deleteFileDialog;

    private ListenerKeyVertical listenerKeyVertical;
    private ListenerKeyHorizontal listenerKeyHorizontal;

    private int selectedListIndex;

    private boolean isMouseScrolled = false;

    @Override
    void setupScreen() {
        Utils.getProfileManager().loadAllProfiles();
        profiles = Utils.getProfileManager().getProfileList();

        setFontColor();
        createTables();
        progressLostDialog = new DialogQuestion(this::openWorldScreen, LOAD_MESSAGE);
        deleteFileDialog = new DialogQuestion(this::deleteSaveFile, DELETE_MESSAGE);
        applyListeners();

        stage.addActor(topTable);
        stage.addActor(table);
        stage.setKeyboardFocus(scrollPane);
        stage.setScrollFocus(scrollPane);

        selectedListIndex = 0;
        selectedMenuIndex = 0;
        setCurrentTextButtonToSelected();
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
        progressLostDialog.update(); // for updating the index in de listener.
        deleteFileDialog.update();
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

    private void selectMenuItem() {
        switch (selectedMenuIndex) {
            case 0 -> processLoadButton();
            case 1 -> processDeleteButton();
            case 2 -> processBackButton();
            default -> throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processLoadButton() {
        Object profileName = listItems.getSelected();
        if (profileName != null) {
            if (startScreen.equals(ScreenType.MENU_PAUSE)) {
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
        } else {
            Utils.getAudioManager().handle(AudioCommand.SE_STOP, AudioEvent.SE_MENU_CONFIRM);
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR);
        }
    }

    private void openWorldScreen() {
        Object profileName = listItems.getSelected();
        Utils.getMapManager().disposeOldMap();
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

    private void createTables() {
        var spriteTransparent = new Sprite(Utils.getResourceManager().getTextureAsset(SPRITE_TRANSPARENT));
        // styles
        var titleStyle = new Label.LabelStyle(menuFont, fontColor);
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = fontColor;
        var listStyle = new List.ListStyle();
        listStyle.font = menuFont;
        listStyle.fontColorSelected = Constant.DARK_RED;
        listStyle.fontColorUnselected = fontColor;
        listStyle.background = new SpriteDrawable(spriteTransparent);
        listStyle.selection = new SpriteDrawable(spriteTransparent);

        // actors
        var titleLabel = new Label(TITLE_LABEL, titleStyle);
        loadButton = new TextButton(MENU_ITEM_LOAD, new TextButton.TextButtonStyle(buttonStyle));
        deleteButton = new TextButton(MENU_ITEM_DELETE, new TextButton.TextButtonStyle(buttonStyle));
        backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        listItems = new List<>(listStyle);
        listItems.setItems(profiles);
        listItems.setAlignment(Align.center);
        scrollPane = new ScrollPane(listItems);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);

        // tables
        topTable = new Table();
        topTable.setFillParent(true);
        topTable.top().padTop(PAD_TOP).right().padRight(PAD_RIGHT);
        topTable.add(titleLabel).spaceBottom(TITLE_SPACE_BOTTOM).row();
        topTable.add(scrollPane);
        topTable.getCell(scrollPane).height(SCROLL_PANE_HEIGHT);

        // bottom table
        table = new Table();
        table.setFillParent(true);
        table.top().padTop(TITLE_SPACE_BOTTOM + topTable.getPrefHeight()).right().padRight(PAD_RIGHT);
        table.add(loadButton).spaceRight(BUTTON_SPACE_RIGHT);
        table.add(deleteButton).spaceRight(BUTTON_SPACE_RIGHT);
        table.add(backButton);
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
