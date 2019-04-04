package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Setter;
import nl.t64.game.rpg.SpriteConfig;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.components.party.PartyContainer;

import java.util.stream.IntStream;


public class PartyWindow {

    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 10;
    private static final int FACE_SIZE = 144;
    private static final int PADDING = 10;
    private static final int MAX_PARTY_SIZE = 5;
    private static final int WINDOW_WIDTH = (FACE_SIZE * MAX_PARTY_SIZE) + (PADDING * (MAX_PARTY_SIZE - 1));

    @Setter
    private PartyContainer party;
    private Stage stage;
    private Window window;
    private ShapeRenderer shapeRenderer;

    public PartyWindow() {
        this.shapeRenderer = new ShapeRenderer();
        var camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        var viewport = new ScreenViewport(camera);
        this.stage = new Stage(viewport);

        this.window = createWindow();
        this.window.setPosition((Gdx.graphics.getWidth() - WINDOW_WIDTH) / 2f, PADDING);
        this.window.setVisible(false);
        this.stage.addActor(this.window);
    }

    public void showHide() {
        if (window.isVisible()) {
            window.setVisible(false);
        } else {
            window.setVisible(true);
        }
    }

    public void render(float dt) {
        if (window.isVisible()) {
            renderWindow();
            stage.act(dt);
            stage.draw();
        }
    }

    private Window createWindow() {
        var menuFont = Utility.getTrueTypeAsset(MENU_FONT, MENU_SIZE);
        var windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = menuFont;
        windowStyle.titleFontColor = Color.BLACK;

        var newWindow = new Window("", windowStyle);
        newWindow.setSize(WINDOW_WIDTH, FACE_SIZE);
        return newWindow;
    }

    private void renderWindow() {
        window.clear();
        renderRectangles();
        renderFaces();
    }

    private void renderRectangles() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        IntStream.rangeClosed(0, MAX_PARTY_SIZE - 1)
                 .forEach(i -> shapeRenderer.rect(window.getX() + (i * FACE_SIZE) + (i * PADDING),
                                                  PADDING, FACE_SIZE, FACE_SIZE));
        shapeRenderer.end();
    }

    private void renderFaces() {
        int i = 0;
        for (String heroId : party.getAllHeroIds()) {
            SpriteConfig faceConfig = Utility.getSpriteConfig(heroId);
            var path = faceConfig.getFacePath();
            var row = faceConfig.getRow() - 1;
            var col = faceConfig.getCol() - 1;

            Texture texture = Utility.getTextureAsset(path);
            TextureRegion[][] splitOfEight = TextureRegion.split(texture, FACE_SIZE, FACE_SIZE);
            TextureRegion heroFace = splitOfEight[row][col];
            var sprite = new Sprite(heroFace);
            var image = new Image(sprite);
            image.setColor(1f, 1f, 1f, 0.5f);
            image.setPosition((i * (float) FACE_SIZE) + (i * PADDING), 0f);
            window.addActor(image);
            i++;
        }
    }

}
