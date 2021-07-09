package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.cutscene.CutsceneId;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;


public class MenuLoad extends MenuScreen {

    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";

    private static final String TITLE_LABEL = "Select profile";
    private static final String MENU_ITEM_START = "Start";
    private static final String MENU_ITEM_LOAD = "Load";
    private static final String MENU_ITEM_DELETE = "Delete";
    private static final String MENU_ITEM_BACK = "Back";
    private static final String LOAD_MESSAGE = """
            All progress after the last save will be lost.
            Are you sure you want to load this profile?""";
    private static final String DELETE_MESSAGE = """
            This save file will be removed.
            Are you sure?""";

    private static final float MENU_X = 604f;
    private static final float TITLE_SPACE_BOTTOM = 10f;
    private static final float BUTTON_SPACE_RIGHT = 20f;

    private static final int NUMBER_OF_ITEMS = 3;
    private static final int DELETE_INDEX = 1;
    private static final int EXIT_INDEX = 2;

    private Array<String> profiles;

    private Table topTable;
    private List<String> listItems;
    private VerticalGroup group;

    private ListenerKeyVertical listenerKeyVertical;
    private ListenerKeyHorizontal listenerKeyHorizontal;

    private int selectedListIndex = 0;

    private boolean isBgmFading = false;
    private boolean isLoaded;

    @Override
    void setupScreen() {
        isLoaded = false;
        profiles = Utils.getProfileManager().getVisualLoadingArray();

        setFontColor();
        createTables();
        createSomeListeners();

        stage.addActor(topTable);
        stage.addActor(table);
        stage.setKeyboardFocus(group);
        stage.addAction(Actions.alpha(1f));

        super.selectedMenuIndex = 0;
        setCurrentTextButtonToSelected();

        new Thread(this::loadProfiles).start();
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(dt);
        if (isLoaded) {
            selectedListIndex = listItems.getSelectedIndex();
        }
        listenerKeyVertical.updateSelectedIndex(selectedListIndex);
        listenerKeyHorizontal.updateSelectedIndex(selectedMenuIndex);
        if (isBgmFading) {
            Utils.getAudioManager().fadeBgmBgs();
        }
        stage.draw();
    }

    private void loadProfiles() {
        profiles = Utils.getProfileManager().getVisualProfileArray();
        listItems.setItems(profiles);
        listItems.setSelectedIndex(selectedListIndex);
        applyAllListeners();
        isLoaded = true;
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
            new DialogQuestion(this::deleteSaveFile, DELETE_MESSAGE).show(stage);
        } else {
            errorSound();
        }
    }

    private void loadGame() {
        if (startScreen.equals(ScreenType.MENU_PAUSE)) {
            new DialogQuestion(this::fadeBeforeOpenWorldScreen, LOAD_MESSAGE).show(stage);
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
        Utils.getScreenManager().getWorldScreen();  // just load the constructor.
        Utils.getProfileManager().loadProfile(selectedListIndex);
        if (Utils.getGameData().getCutscenes().isPlayed(CutsceneId.SCENE_INTRO)) {
            Utils.getScreenManager().setScreen(ScreenType.WORLD);
        } else {
            Utils.getGameData().getCutscenes().setPlayed(CutsceneId.SCENE_INTRO);
            Utils.getScreenManager().setScreen(ScreenType.SCENE_INTRO);
        }
    }

    private void deleteSaveFile() {
        Utils.getProfileManager().removeProfile(selectedListIndex);
        profiles = Utils.getProfileManager().getVisualProfileArray();
        listItems.setItems(profiles);
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
        listItems.setAlignment(Align.center);
        group = new VerticalGroup();
        group.addActor(listItems);

        // tables
        topTable = new Table();
        topTable.setFillParent(true);
        topTable.add(titleLabel).center().spaceBottom(TITLE_SPACE_BOTTOM).row();
        topTable.add(group).center();
        topTable.setX(MENU_X);
        Actor logo = stage.getActors().peek();
        topTable.top().padTop((logo.getHeight() * logo.getScaleY()) + LOGO_PAD + PAD_TOP);

        // bottom table
        table = new Table();
        table.setFillParent(true);
        table.add(loadButton).spaceRight(BUTTON_SPACE_RIGHT);
        table.add(deleteButton).spaceRight(BUTTON_SPACE_RIGHT);
        table.add(backButton);
        table.top().padTop(TITLE_SPACE_BOTTOM + topTable.getPrefHeight())
             .right().padRight(((logo.getWidth() * logo.getScaleX()) / 2f) - (table.getPrefWidth() / 2f) + LOGO_PAD);
    }

    private void createSomeListeners() {
        listenerKeyVertical = new ListenerKeyVertical(newIndex -> listItems.setSelectedIndex(newIndex), listItems.getItems().size);
        listenerKeyHorizontal = new ListenerKeyHorizontal(super::updateMenuIndex, NUMBER_OF_ITEMS);
    }

    private void applyAllListeners() {
        var listenerKeyConfirm = new ListenerKeyConfirm(this::selectMenuItem);
        var listenerKeyDelete = new ListenerKeyDelete(super::updateMenuIndex, this::selectMenuItem, DELETE_INDEX);
        var listenerKeyCancel = new ListenerKeyCancel(super::updateMenuIndex, this::selectMenuItem, EXIT_INDEX);
        group.addListener(listenerKeyVertical);
        group.addListener(listenerKeyHorizontal);
        group.addListener(listenerKeyConfirm);
        group.addListener(listenerKeyDelete);
        group.addListener(listenerKeyCancel);
    }

}
