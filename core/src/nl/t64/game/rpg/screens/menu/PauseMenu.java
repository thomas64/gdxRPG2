package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.listeners.ButtonMouseListener;
import nl.t64.game.rpg.listeners.ConfirmKeyListener;
import nl.t64.game.rpg.listeners.VerticalKeyListener;


public class PauseMenu implements Screen {

    private static final Color TRANSPARENT = new Color(0f, 0f, 0f, 0.85f);
    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;
    private static final int MENU_SPACE_BOTTOM = 10;

    private static final String MENU_ITEM_CONTINUE = "Continue";
    private static final String MENU_ITEM_LOAD_GAME = "Load Game";
    private static final String MENU_ITEM_SETTINGS = "Settings";
    private static final String MENU_ITEM_MAIN_MENU = "Main Menu";

    private static final int NUMBER_OF_ITEMS = 4;
    private static final int EXIT_INDEX = 0;

    private Stage stage;

    private Image screenshot;
    private Image blur;
    private boolean hasBackground = false;

    private BitmapFont menuFont;
    private Table table;
    private TextButton continueButton;
    private TextButton loadGameButton;
    private TextButton settingsButton;
    private TextButton mainMenuButton;

    private VerticalKeyListener verticalKeyListener;

    private int selectedIndex;

    public PauseMenu() {
        this.stage = new Stage();
        createFonts();
        this.selectedIndex = 0;
    }

    public void setBackground(Image screenshot, Image blur) {
        this.screenshot = screenshot;
        this.blur = blur;
        stage.addActor(screenshot);
        stage.addActor(blur);
        hasBackground = true;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        setupScreen();
    }

    private void setupScreen() {
        setBackground();
        this.table = createTable();
        applyListeners();
        this.stage.addActor(this.table);
        this.stage.setKeyboardFocus(this.table);
        setCurrentTextButtonToRed();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        verticalKeyListener.updateSelectedIndex(selectedIndex);
        stage.draw();
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

    public void updateIndex(Integer newIndex) {
        selectedIndex = newIndex;
        setAllTextButtonsToWhite();
        setCurrentTextButtonToRed();
    }

    private void selectMenuItem() {
        GdxRpg2 game = GdxRpg2.getInstance();
        switch (selectedIndex) {
            case 0:
                game.setScreen(game.getWorldScreen());
                break;
            case 1:
                game.getLoadGameMenuScreen().setBackground(screenshot, blur);
                game.setScreen(game.getLoadGameMenuScreen());
                game.getLoadGameMenuScreen().setFromScreen(this);
                break;
            case 2:
                game.getSettingsMenuScreen().setBackground(screenshot, blur);
                game.setScreen(game.getSettingsMenuScreen());
                game.getSettingsMenuScreen().setFromScreen(this);
                break;
            case 3:
                game.setScreen(game.getMainMenuScreen());
                break;
            default:
                throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void setAllTextButtonsToWhite() {
        for (Actor actor : table.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = Color.WHITE;
        }
    }

    private void setCurrentTextButtonToRed() {
        ((TextButton) table.getChildren().get(selectedIndex)).getStyle().fontColor = Constant.DARK_RED;
    }

    private void createFonts() {
        Utility.loadTrueTypeAsset(MENU_FONT, MENU_SIZE);
        menuFont = Utility.getTrueTypeAsset(MENU_FONT);
    }

    private void setBackground() {
        if (!hasBackground) {
            screenshot = new Image(ScreenUtils.getFrameBufferTexture());
            blur = createBlur();
            stage.addActor(screenshot);
            stage.addActor(blur);
        }
        hasBackground = false;
    }

    private Image createBlur() {
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.Alpha);
        pixmap.setColor(TRANSPARENT);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        Sprite sprite = new Sprite(texture);
        return new Image(new SpriteDrawable(sprite));
    }

    private Table createTable() {
        // styles
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;

        // actors
        continueButton = new TextButton(MENU_ITEM_CONTINUE, new TextButton.TextButtonStyle(buttonStyle));
        loadGameButton = new TextButton(MENU_ITEM_LOAD_GAME, new TextButton.TextButtonStyle(buttonStyle));
        settingsButton = new TextButton(MENU_ITEM_SETTINGS, new TextButton.TextButtonStyle(buttonStyle));
        mainMenuButton = new TextButton(MENU_ITEM_MAIN_MENU, new TextButton.TextButtonStyle(buttonStyle));

        // table
        Table newTable = new Table();
        newTable.setFillParent(true);
        newTable.add(continueButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(loadGameButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(settingsButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(mainMenuButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        return newTable;
    }

    private void applyListeners() {
        verticalKeyListener = new VerticalKeyListener(this::updateIndex, NUMBER_OF_ITEMS);
        table.addListener(verticalKeyListener);
        table.addListener(new ConfirmKeyListener(this::updateIndex, this::selectMenuItem, EXIT_INDEX));
        continueButton.addListener(createButtonMouseListener(0));
        loadGameButton.addListener(createButtonMouseListener(1));
        settingsButton.addListener(createButtonMouseListener(2));
        mainMenuButton.addListener(createButtonMouseListener(3));
    }

    private ButtonMouseListener createButtonMouseListener(int index) {
        return new ButtonMouseListener(this::updateIndex, this::selectMenuItem, index);
    }

}
