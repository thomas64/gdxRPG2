package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;


public class MenuLoad extends MenuScreen {

    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";

    private static final String TITLE_LABEL = "Select profile";
    private static final String MENU_ITEM_START = "Start";
    private static final String MENU_ITEM_LOAD = "Load";
    private static final String MENU_ITEM_DELETE = "Delete";
    private static final String MENU_ITEM_BACK = "Back";
    private static final String LOAD_MESSAGE = "All progress after the last save will be lost."
                                               + System.lineSeparator() + "Are you sure you want to load this profile?";
    private static final String DELETE_MESSAGE = "This save file will be removed."
                                                 + System.lineSeparator() + "Are you sure?";

    private static final float TITLE_SPACE_BOTTOM = 10f;
    private static final float BUTTON_SPACE_RIGHT = 20f;

    private static final int NUMBER_OF_ITEMS = 3;
    private static final int DELETE_INDEX = 1;
    private static final int EXIT_INDEX = 2;

    private Array<String> profiles;

    private Table topTable;
    private List<String> listItems;
    private ScrollPane scrollPane;
    private DialogQuestion progressLostDialog;
    private DialogQuestion deleteFileDialog;

    private ListenerKeyVertical listenerKeyVertical;
    private ListenerKeyHorizontal listenerKeyHorizontal;

    private int selectedListIndex = 0;

    private boolean isBgmFading = false;

    @Override
    void setupScreen() {
        profiles = Utils.getProfileManager().getVisualProfileArray();

        setFontColor();
        createTables();
        progressLostDialog = new DialogQuestion(this::fadeBeforeOpenWorldScreen, LOAD_MESSAGE);
        deleteFileDialog = new DialogQuestion(this::deleteSaveFile, DELETE_MESSAGE);
        applyListeners();

        stage.addActor(topTable);
        stage.addActor(table);
        stage.setKeyboardFocus(scrollPane);
        stage.setScrollFocus(scrollPane);
        stage.addAction(Actions.alpha(1f));

        selectedMenuIndex = 0;
        setCurrentTextButtonToSelected();
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(dt);
        selectedListIndex = listItems.getSelectedIndex();
        scrollScrollPane();
        listenerKeyVertical.updateSelectedIndex(selectedListIndex);
        listenerKeyHorizontal.updateSelectedIndex(selectedMenuIndex);
        progressLostDialog.update(); // for updating the index in de listener.
        deleteFileDialog.update();
        if (isBgmFading) {
            Utils.getAudioManager().fadeBgmBgs();
        }
        stage.draw();
    }

    private void scrollScrollPane() {
        float itemHeight = listItems.getItemHeight();
        float listHeight = itemHeight * listItems.getItems().size;
        float selectedY = listHeight - (itemHeight * (selectedListIndex + 1f));
        scrollPane.scrollTo(0, selectedY, 0, 0, false, true);
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
        if (Utils.getProfileManager().doesProfileExist(selectedListIndex)) {
            loadGame();
        } else if (startScreen.equals(ScreenType.MENU_MAIN)) {
            newGame();
        } else {
            errorSound();
        }
    }

    private void processDeleteButton() {
        if (Utils.getProfileManager().doesProfileExist(selectedListIndex)) {
            deleteFileDialog.show(stage);
        } else {
            errorSound();
        }
    }

    private void loadGame() {
        if (startScreen.equals(ScreenType.MENU_PAUSE)) {
            progressLostDialog.show(stage);
        } else {
            fadeBeforeOpenWorldScreen();
        }
    }

    private void newGame() {
        Utils.getProfileManager().setSelectedIndex(selectedListIndex);
        processButton(ScreenType.MENU_LOAD, ScreenType.MENU_NEW);
    }

    private void errorSound() {
        Utils.getAudioManager().handle(AudioCommand.SE_STOP, AudioEvent.SE_MENU_CONFIRM);
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR);
    }

    private void fadeBeforeOpenWorldScreen() {
        Gdx.input.setInputProcessor(null);
        stage.addAction(Actions.sequence(Actions.run(() -> isBgmFading = true),
                                         Actions.fadeOut(Constant.FADE_DURATION),
                                         Actions.run(() -> isBgmFading = false),
                                         Actions.run(this::openWorldScreen)));
    }

    private void openWorldScreen() {
        Utils.getMapManager().disposeOldMaps();
        Utils.getScreenManager().setScreen(ScreenType.WORLD);
        Utils.getProfileManager().loadProfile(selectedListIndex);
    }

    private void deleteSaveFile() {
        Utils.getProfileManager().removeProfile(selectedListIndex);
        profiles = Utils.getProfileManager().getVisualProfileArray();
        listItems.setItems(profiles);
        scrollPane.clearChildren();
        scrollPane.setActor(listItems);
        listItems.setSelectedIndex(selectedListIndex);
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
        var loadButton = new TextButton(startScreen.equals(ScreenType.MENU_PAUSE) ? MENU_ITEM_LOAD : MENU_ITEM_START,
                                        new TextButton.TextButtonStyle(buttonStyle));
        var deleteButton = new TextButton(MENU_ITEM_DELETE, new TextButton.TextButtonStyle(buttonStyle));
        var backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        listItems = new List<>(listStyle);
        listItems.setItems(profiles);
        listItems.setAlignment(Align.right);
        listItems.setSelectedIndex(selectedListIndex);
        scrollPane = new ScrollPane(listItems);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);

        // tables
        topTable = new Table();
        topTable.setFillParent(true);
        topTable.top().padTop(PAD_TOP).right().padRight(PAD_RIGHT);
        topTable.add(titleLabel).right().spaceBottom(TITLE_SPACE_BOTTOM).row();
        topTable.add(scrollPane).right();

        // bottom table
        table = new Table();
        table.setFillParent(true);
        table.top().padTop(TITLE_SPACE_BOTTOM + topTable.getPrefHeight()).right().padRight(PAD_RIGHT);
        table.add(loadButton).spaceRight(BUTTON_SPACE_RIGHT);
        table.add(deleteButton).spaceRight(BUTTON_SPACE_RIGHT);
        table.add(backButton);
    }

    private void applyListeners() {
        listenerKeyVertical = new ListenerKeyVertical(newIndex -> listItems.setSelectedIndex(newIndex), listItems.getItems().size);
        listenerKeyHorizontal = new ListenerKeyHorizontal(super::updateMenuIndex, NUMBER_OF_ITEMS);
        var listenerKeyConfirm = new ListenerKeyConfirm(this::selectMenuItem);
        var listenerKeyDelete = new ListenerKeyDelete(super::updateMenuIndex, this::selectMenuItem, DELETE_INDEX);
        var listenerKeyCancel = new ListenerKeyCancel(super::updateMenuIndex, this::selectMenuItem, EXIT_INDEX);
        scrollPane.addListener(listenerKeyVertical);
        scrollPane.addListener(listenerKeyHorizontal);
        scrollPane.addListener(listenerKeyConfirm);
        scrollPane.addListener(listenerKeyDelete);
        scrollPane.addListener(listenerKeyCancel);
    }

}
