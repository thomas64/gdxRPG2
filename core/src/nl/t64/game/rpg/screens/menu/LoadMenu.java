package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import lombok.Setter;
import nl.t64.game.rpg.Engine;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.listeners.*;


public class LoadMenu implements Screen {

    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";
    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;

    private static final String TITLE_LABEL = "Select your profile:";
    private static final String MENU_ITEM_LOAD = "Load";
    private static final String MENU_ITEM_BACK = "Back";

    private static final int TITLE_SPACE_BOTTOM = 30;
    private static final int TOP_TABLE_Y = 50;
    private static final int SCROLL_PANE_HEIGHT = 380;
    private static final int BOTTOM_TABLE_Y = 100;
    private static final int LOAD_BUTTON_SPACE_RIGHT = 150;

    private static final int NUMBER_OF_ITEMS = 2;
    private static final int EXIT_INDEX = 1;

    private Engine engine;
    private Stage stage;

    @Setter
    private Screen fromScreen;

    private Array<String> profiles;

    private Image screenshot;
    private Image blur;

    private BitmapFont menuFont;
    private Table topTable;
    private List listItems;
    private ScrollPane scrollPane;
    private Table bottomTable;
    private TextButton loadButton;
    private TextButton backButton;
    private ProgressLostDialog progressLostDialog;

    private VerticalKeyListener verticalKeyListener;
    private HorizontalKeyListener horizontalKeyListener;

    private int lastSelectedListIndex;
    private int currentSelectedListIndex;
    private int selectedMenuIndex;

    private boolean isMouseScrolled = false;

    public LoadMenu(Engine engine) {
        this.engine = engine;
        this.stage = new Stage();
        createFonts();
    }

    public void setBackground(Image screenshot, Image blur) {
        this.screenshot = screenshot;
        this.blur = blur;
        stage.addActor(screenshot);
        stage.addActor(blur);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        setupScreen();
    }

    private void setupScreen() {
        engine.getProfileManager().loadAllProfiles();
        profiles = engine.getProfileManager().getProfileList();

        createTables();
        this.progressLostDialog = new ProgressLostDialog(this::openWorldScreen);
        applyListeners();

        this.stage.addActor(this.topTable);
        this.stage.addActor(this.bottomTable);
        this.stage.setKeyboardFocus(this.scrollPane);
        this.stage.setScrollFocus(this.scrollPane);

        this.lastSelectedListIndex = 0;
        this.currentSelectedListIndex = 0;

        this.selectedMenuIndex = 0;
        setCurrentTextButtonToRed();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        currentSelectedListIndex = listItems.getSelectedIndex();
        scrollScrollPane();
        verticalKeyListener.updateSelectedIndex(currentSelectedListIndex);
        horizontalKeyListener.updateSelectedIndex(selectedMenuIndex);
        progressLostDialog.update(); // for updating the index in de listener.
        stage.draw();
    }

    private void scrollScrollPane() {
        if (!isMouseScrolled) {
            float itemHeight = listItems.getItemHeight();
            float listHeight = itemHeight * listItems.getItems().size;
            float yScroll;
            if (lastSelectedListIndex < currentSelectedListIndex) {
                yScroll = (listHeight - (itemHeight * 2)) - (itemHeight * currentSelectedListIndex);
            } else {
                yScroll = (listHeight + itemHeight) - (itemHeight * currentSelectedListIndex);
            }
            scrollPane.scrollTo(0, yScroll, 0, 0);
            lastSelectedListIndex = currentSelectedListIndex;
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void pause() {
        // empty
    }

    @Override
    public void resume() {
        // empty
    }

    @Override
    public void hide() {
        stage.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // menuFont.dispose(); is already disposed in MainMenu?
        stage.clear();
        stage.dispose();
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
                processBackButton();
                break;
            default:
                throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processLoadButton() {
        Object profileName = listItems.getSelected();
        if (profileName != null) {
            if (fromScreen instanceof PauseMenu) {
                progressLostDialog.show(stage);
            } else {
                openWorldScreen();
            }
        }
    }

    private void processBackButton() {
        if (fromScreen instanceof PauseMenu) {
            ((PauseMenu) fromScreen).setBackground(screenshot, blur);
        }
        engine.setScreen(fromScreen);
        fromScreen = null;
    }

    private void openWorldScreen() {
        Object profileName = listItems.getSelected();
        engine.getProfileManager().loadProfile(profileName.toString());
        engine.setScreen(engine.getWorldScreen());
    }

    private void setAllTextButtonsToWhite() {
        for (Actor actor : bottomTable.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = Color.WHITE;
        }
    }

    private void setCurrentTextButtonToRed() {
        ((TextButton) bottomTable.getChildren().get(selectedMenuIndex)).getStyle().fontColor = Constant.DARK_RED;
    }

    private void createFonts() {
        Utility.loadTrueTypeAsset(MENU_FONT, MENU_SIZE);
        menuFont = Utility.getTrueTypeAsset(MENU_FONT);
    }

    private void createTables() {
        // styles
        Label.LabelStyle titleStyle = new Label.LabelStyle(menuFont, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;
        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = menuFont;
        listStyle.fontColorSelected = Constant.DARK_RED;
        listStyle.fontColorUnselected = Color.WHITE;
        Utility.loadTextureAsset(SPRITE_TRANSPARENT);
        Texture textureTransparent = Utility.getTextureAsset(SPRITE_TRANSPARENT);
        Sprite spriteTransparent = new Sprite(textureTransparent);
        listStyle.background = new SpriteDrawable(spriteTransparent);
        listStyle.selection = new SpriteDrawable(spriteTransparent);

        // actors
        Label titleLabel = new Label(TITLE_LABEL, titleStyle);
        loadButton = new TextButton(MENU_ITEM_LOAD, new TextButton.TextButtonStyle(buttonStyle));
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
        bottomTable.setHeight(loadButton.getHeight());
        bottomTable.setWidth(Gdx.graphics.getWidth());
        bottomTable.setY(BOTTOM_TABLE_Y);
        bottomTable.add(loadButton).spaceRight(LOAD_BUTTON_SPACE_RIGHT);
        bottomTable.add(backButton);
    }

    private void applyListeners() {
        verticalKeyListener = new VerticalKeyListener(this::updateListIndex, listItems.getItems().size);
        horizontalKeyListener = new HorizontalKeyListener(this::updateMenuIndex, NUMBER_OF_ITEMS);
        scrollPane.addListener(verticalKeyListener);
        scrollPane.addListener(horizontalKeyListener);
        scrollPane.addListener(new ConfirmKeyListener(this::updateMenuIndex, this::selectMenuItem, EXIT_INDEX));
        scrollPane.addListener(new MouseScrollListener(this::busyScrolling));
        loadButton.addListener(createButtonMouseListener(0));
        backButton.addListener(createButtonMouseListener(1));
    }

    private ButtonMouseListener createButtonMouseListener(int index) {
        return new ButtonMouseListener(this::updateMenuIndex, this::selectMenuItem, index);
    }

}
