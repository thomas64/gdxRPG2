package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.ScreenType;


public class MainMenuScreen implements Screen {

    private static final Color DARK_RED = new Color(0.5f, 0f, 0f, 1f);
    private static final String TITLE = "gdxRPG2";
    private static final String TITLE_FONT = "fonts/colonna.ttf";
    private static final int TITLE_SIZE = 200;
    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;

    private Stage stage;
    private GdxRpg2 game;
    private Table table;

    private int selectedIndex;


    public MainMenuScreen(GdxRpg2 game) {
        this.game = game;
        this.stage = new Stage();
        this.table = new Table();
        this.table.setFillParent(true);
        this.stage.addActor(table);

        this.fillTable();

        this.stage.setKeyboardFocus(this.table);
        this.selectedIndex = 1;
        ((TextButton) this.table.getChildren().get(this.selectedIndex)).getStyle().fontColor = DARK_RED;

        this.addListeners();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
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

    private void addListeners() {

        table.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {

                switch (keycode) {
                    case Input.Keys.UP:
                        if (selectedIndex <= 1) {
                            selectedIndex = 1;
                        } else {
                            selectedIndex -= 1;
                        }
                        break;
                    case Input.Keys.DOWN:
                        if (selectedIndex >= table.getRows() - 1) {
                            selectedIndex = table.getRows() - 1;
                        } else {
                            selectedIndex += 1;
                        }
                        break;
                    case Input.Keys.ENTER:
                        selectMenuItem();
                        break;
                    default:
                }
                setAllTextButtonsToWhite();
                setCurrentTextButtonToRed();
                return true;
            }
        });

        int i = 0;
        for (Actor actor : table.getChildren()) {
            if (actor instanceof TextButton) {
                int finalI = i;
                actor.addListener(new InputListener() {
                    @Override
                    public boolean mouseMoved(InputEvent event, float x, float y) {
                        selectedIndex = finalI;
                        setAllTextButtonsToWhite();
                        setCurrentTextButtonToRed();
                        return true;
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        selectMenuItem();
                        return true;
                    }
                });
            }
            i++;
        }
    }

    private void selectMenuItem() {
        switch (selectedIndex) {
            case 1:
                game.setScreen(game.getScreenType(ScreenType.NEW_GAME));
                break;
            case 2:
                game.setScreen(game.getScreenType(ScreenType.LOAD_GAME));
                break;
            case 3:
                game.setScreen(game.getScreenType(ScreenType.SETTINGS));
                break;
            case 4:
                Gdx.app.exit();
                break;
            default:
        }
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
