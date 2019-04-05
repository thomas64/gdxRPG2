package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Setter;
import nl.t64.game.rpg.SpriteConfig;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;

import java.util.Map;
import java.util.stream.IntStream;


public class PartyWindow {

    private static final String FONT_PATH = "fonts/keep_singing.ttf";
    private static final Color TRANSPARENT_BLACK = new Color(0f, 0f, 0f, 0.65f);
    private static final Color TRANSPARENT_WHITE = new Color(1f, 1f, 1f, 0.5f);
    private static final int FONT_SIZE = 22;
    private static final int MAX_PARTY_SIZE = 5;

    private static final float LINE_HEIGHT = 22f;
    private static final float FACE_SIZE = 144f;
    private static final float PADDING = 10f;
    private static final float PADDING_SMALL = 5f;
    private static final float FACE_Y = 90f;
    private static final float WINDOW_WIDTH = (FACE_SIZE * MAX_PARTY_SIZE) + (PADDING * (MAX_PARTY_SIZE - 1f));
    private static final float WINDOW_HEIGHT = FACE_Y + FACE_SIZE;
    private static final float HIGH_Y = 0f;
    private static final float LOW_Y = -WINDOW_HEIGHT;

    private static final float BAR_X = 55f;
    private static final float BAR_Y = -4f;
    private static final float BAR_WIDTH = 85f;
    private static final float BAR_HEIGHT = 12f;

    private static final float VELOCITY = 800f;

    @Setter
    private PartyContainer party;
    private Stage stage;
    private Window window;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private float yPos;
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;


    public PartyWindow() {
        createFonts();
        this.shapeRenderer = new ShapeRenderer();
        this.yPos = LOW_Y;
        var camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        var viewport = new ScreenViewport(camera);
        this.stage = new Stage(viewport);

        this.window = createWindow();
        this.window.setPosition((Gdx.graphics.getWidth() - WINDOW_WIDTH) / 2f, 0f);
        this.window.setVisible(false);
        this.stage.addActor(this.window);
    }

    public void showHide() {
        if (!isMovingDown && !isMovingUp) {

            if (window.isVisible()) {
                isMovingDown = true;
            } else {
                window.setVisible(true);
                isMovingUp = true;
            }

        }
    }

    public void render(float dt) {
        if (isMovingUp) {
            yPos += VELOCITY * dt;
            if (yPos >= HIGH_Y) {
                yPos = HIGH_Y;
                isMovingUp = false;
            }
        }

        if (isMovingDown) {
            yPos -= VELOCITY * dt;
            if (yPos <= LOW_Y) {
                yPos = LOW_Y;
                isMovingDown = false;
                window.setVisible(false);
            }
        }

        if (window.isVisible()) {
            renderWindow();
            stage.act(dt);
            stage.draw();
        }
    }

    private void createFonts() {
        font = Utility.getTrueTypeAsset(FONT_PATH, FONT_SIZE);
    }

    private Window createWindow() {
        var windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.titleFontColor = Color.BLACK;

        var newWindow = new Window("", windowStyle);
        newWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        return newWindow;
    }

    private void renderWindow() {
        window.clear();
        renderBorders();
        renderFaces();
        renderNames();
        renderLevels();
        renderHpLabels();
        renderHpBars();
        renderXpLabels();
        renderXpBars();
    }

    private void renderBorders() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(TRANSPARENT_BLACK);
        renderSquares();
        renderHorizontalLines();
        shapeRenderer.end();
    }

    private void renderSquares() {
        IntStream.rangeClosed(0, MAX_PARTY_SIZE - 1)
                 .forEach(i -> shapeRenderer.rect(window.getX() + (i * FACE_SIZE) + (i * PADDING),
                                                  yPos + PADDING,
                                                  FACE_SIZE,
                                                  WINDOW_HEIGHT - PADDING));
    }

    private void renderHorizontalLines() {
        IntStream.rangeClosed(0, party.getSize() - 1)
                 .forEach(i -> shapeRenderer.line(window.getX() + (i * FACE_SIZE) + (i * PADDING),
                                                  yPos + FACE_Y,
                                                  window.getX() + (i * FACE_SIZE) + (i * PADDING) + FACE_SIZE,
                                                  yPos + FACE_Y));
    }

    private void renderFaces() {
        int i = 0;
        for (String heroId : party.getAllHeroIds()) {
            SpriteConfig faceConfig = Utility.getSpriteConfig(heroId);
            String path = faceConfig.getFacePath();
            int row = faceConfig.getRow() - 1;
            int col = faceConfig.getCol() - 1;

            Texture texture = Utility.getTextureAsset(path);
            TextureRegion[][] splitOfEight = TextureRegion.split(texture, (int) FACE_SIZE, (int) FACE_SIZE);
            TextureRegion heroFace = splitOfEight[row][col];
            var sprite = new Sprite(heroFace);
            var image = new Image(sprite);
            image.setColor(TRANSPARENT_WHITE);
            image.setPosition((i * FACE_SIZE) + (i * PADDING), yPos + FACE_Y);
            window.addActor(image);
            i++;
        }
    }

    private void renderNames() {
        var labelStyle = new Label.LabelStyle(font, TRANSPARENT_BLACK);
        int i = 0;
        for (String heroName : party.getAllHeroNames()) {
            var heroLabel = new Label(heroName, labelStyle);
            heroLabel.setPosition(i * FACE_SIZE + (i * PADDING) + PADDING_SMALL,
                                  yPos + FACE_Y);
            window.addActor(heroLabel);
            i++;
        }
    }

    private void renderLevels() {
        var labelStyle = new Label.LabelStyle(font, TRANSPARENT_BLACK);
        int i = 0;
        for (Integer level : party.getAllHeroLevels()) {
            var levelLabel = new Label("Level: " + level.toString(), labelStyle);
            levelLabel.setPosition(i * FACE_SIZE + (i * PADDING) + PADDING_SMALL,
                                   yPos + FACE_Y - (1f * LINE_HEIGHT) - PADDING);
            window.addActor(levelLabel);
            i++;
        }
    }

    private void renderHpLabels() {
        var labelStyle = new Label.LabelStyle(font, TRANSPARENT_BLACK);
        IntStream.rangeClosed(0, party.getSize() - 1)
                 .forEach(partyNumber -> {
                     var hpLabel = new Label("HP: ", labelStyle);
                     hpLabel.setPosition(partyNumber * FACE_SIZE + (partyNumber * PADDING) + PADDING_SMALL,
                                         yPos + FACE_Y - (2f * LINE_HEIGHT) - PADDING);
                     window.addActor(hpLabel);
                 });
    }

    private void renderHpBars() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        int i = 0;
        for (HeroItem hero : party.getAllHeroes()) {
            Map<String, Integer> hpStats = hero.getAllHpStats();
            Color color = getHpColor(hpStats);
            shapeRenderer.setColor(color);
            float barWidth = (BAR_WIDTH / hero.getMaximumHp()) * hero.getCurrentHp();
            renderBar(i, 2f, barWidth);
            i++;
        }
        shapeRenderer.end();

        renderBarOutline(2f);
    }

    private Color getHpColor(Map<String, Integer> hpStats) {
        Color color = Color.ROYAL;
        if (hpStats.get("lvlHp") < hpStats.get("lvlCur")) {
            color = Color.LIME;
        }
        if (hpStats.get("staHp") < hpStats.get("staCur")) {
            color = Color.YELLOW;
        }
        if (hpStats.get("eduHp") < hpStats.get("eduCur")) {
            color = Color.FIREBRICK;
            if (hpStats.get("staHp") > 0) {
                color = Color.ORANGE;
            }
        }
        return color;
    }

    private void renderXpLabels() {
        var labelStyle = new Label.LabelStyle(font, TRANSPARENT_BLACK);
        IntStream.rangeClosed(0, party.getSize() - 1)
                 .forEach(partyNumber -> {
                     var xpLabel = new Label("XP: ", labelStyle);
                     xpLabel.setPosition(partyNumber * FACE_SIZE + (partyNumber * PADDING) + PADDING_SMALL,
                                         yPos + FACE_Y - (3f * LINE_HEIGHT) - PADDING);
                     window.addActor(xpLabel);
                 });
    }

    private void renderXpBars() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLD);
        IntStream.rangeClosed(0, party.getSize() - 1)
                 .forEach(partyNumber -> renderBar(partyNumber, 3f, calculateXpBarWidth(partyNumber)));
        shapeRenderer.end();

        renderBarOutline(3f);
    }

    private float calculateXpBarWidth(int i) {
        HeroItem hero = party.getHero(i);
        int maxXp = hero.getXpDeltaBetweenLevels();
        int currentXp = maxXp - hero.getNeededXpForNextLevel();
        return (BAR_WIDTH / maxXp) * currentXp;
    }

    private void renderBarOutline(float linePosition) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(TRANSPARENT_BLACK);
        IntStream.rangeClosed(0, party.getSize() - 1)
                 .forEach(partyNumber -> renderBar(partyNumber, linePosition, BAR_WIDTH));
        shapeRenderer.end();
    }

    private void renderBar(int partyNumber, float linePosition, float barWidth) {
        shapeRenderer.rect(window.getX() + (partyNumber * FACE_SIZE) + (partyNumber * PADDING) + BAR_X,
                           yPos + FACE_Y - (linePosition * LINE_HEIGHT) + BAR_Y,
                           barWidth,
                           BAR_HEIGHT);
    }

}
