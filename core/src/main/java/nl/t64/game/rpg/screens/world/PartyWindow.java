package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.SpriteConfig;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;

import java.util.Map;
import java.util.stream.IntStream;


class PartyWindow {

    private static final String FONT_PATH = "fonts/spectral.ttf";
    private static final String FONT_BIG_PATH = "fonts/spectral_big.ttf";
    private static final Color TRANSPARENT_BLACK = new Color(0f, 0f, 0f, 0.7f);
    private static final Color TRANSPARENT_WHITE = new Color(1f, 1f, 1f, 0.2f);
    private static final Color TRANSPARENT_FACES = new Color(1f, 1f, 1f, 0.6f);

    private static final int FONT_BIG_SIZE = 28;
    private static final int FONT_SIZE = 20;
    private static final int MAX_PARTY_SIZE = 5;

    private static final float LINE_HEIGHT = 22f;
    private static final float FACE_SIZE = 144f;
    private static final float PADDING = 10f;
    private static final float PADDING_SMALL = 5f;
    private static final float PADDING_NAME = 7f;
    private static final float PADDING_LEVEL = 12f;
    private static final float PADDING_LINE = PADDING + PADDING_SMALL;
    private static final float FACE_Y = 90f;
    private static final float TABLE_WIDTH = (FACE_SIZE * MAX_PARTY_SIZE) + (PADDING * (MAX_PARTY_SIZE - 1f));
    private static final float TABLE_HEIGHT = FACE_Y + FACE_SIZE;
    private static final float HIGH_Y = 0f;
    private static final float LOW_Y = -TABLE_HEIGHT;

    private static final float BAR_X = 50f;
    private static final float BAR_Y = -4f;
    private static final float BAR_WIDTH = 85f;
    private static final float BAR_HEIGHT = 12f;

    private static final float VELOCITY = 800f;

    private PartyContainer party;
    private Stage stage;
    private Table table;
    private BitmapFont font;
    private BitmapFont fontBig;
    private ShapeRenderer shapeRenderer;

    private float yPos;
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;


    PartyWindow() {
        createFonts();
        this.shapeRenderer = new ShapeRenderer();
        this.yPos = LOW_Y;
        this.stage = new Stage();

        this.table = new Table();
        this.table.setSize(TABLE_WIDTH, TABLE_HEIGHT);
        this.table.setPosition((Gdx.graphics.getWidth() - TABLE_WIDTH) / 2f, 0f);
        this.table.setVisible(false);
        this.stage.addActor(this.table);
    }

    void dispose() {
        stage.clear();
        stage.dispose();
        font.dispose();
        fontBig.dispose();
        shapeRenderer.dispose();
    }

    void showHide() {
        if (!isMovingDown && !isMovingUp) {
            setVisibility();
        }
    }

    private void setVisibility() {
        if (table.isVisible()) {
            isMovingDown = true;
        } else {
            table.setVisible(true);
            isMovingUp = true;
        }
    }

    void render(float dt) {
        handleMovingUp(dt);
        handleMovingDown(dt);
        handleRendering(dt);
    }

    private void handleMovingUp(float dt) {
        if (isMovingUp) {
            yPos += VELOCITY * dt;
            if (yPos >= HIGH_Y) {
                yPos = HIGH_Y;
                isMovingUp = false;
            }
        }
    }

    private void handleMovingDown(float dt) {
        if (isMovingDown) {
            yPos -= VELOCITY * dt;
            if (yPos <= LOW_Y) {
                yPos = LOW_Y;
                isMovingDown = false;
                table.setVisible(false);
            }
        }
    }

    private void handleRendering(float dt) {
        if (table.isVisible()) {
            renderTable();
            stage.act(dt);
            stage.draw();
        }
    }

    private void createFonts() {
        font = Utils.getResourceManager().getTrueTypeAsset(FONT_PATH, FONT_SIZE);
        fontBig = Utils.getResourceManager().getTrueTypeAsset(FONT_BIG_PATH, FONT_BIG_SIZE);
    }

    private void renderTable() {
        party = Utils.getGameData().getParty();
        table.clear();
        renderBackgrounds();
        renderSquares();
        renderHorizontalLines();
        renderFaces();
        renderNames();
        renderLevelLabels();
        renderHpLabels();
        renderHpBars();
        renderXpLabels();
        renderXpBars();
    }

    private void renderBackgrounds() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(TRANSPARENT_WHITE);
        IntStream.rangeClosed(0, party.getSize() - 1)
                 .forEach(i -> shapeRenderer.rect(table.getX() + (i * FACE_SIZE) + (i * PADDING),
                                                  yPos + PADDING,
                                                  FACE_SIZE,
                                                  FACE_Y - PADDING));
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void renderSquares() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(TRANSPARENT_BLACK);
        IntStream.rangeClosed(0, MAX_PARTY_SIZE - 1)
                 .forEach(i -> shapeRenderer.rect(table.getX() + (i * FACE_SIZE) + (i * PADDING),
                                                  yPos + PADDING,
                                                  FACE_SIZE,
                                                  TABLE_HEIGHT - PADDING));
        shapeRenderer.end();
    }

    private void renderHorizontalLines() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(TRANSPARENT_BLACK);
        IntStream.rangeClosed(0, party.getSize() - 1)
                 .forEach(i -> shapeRenderer.line(table.getX() + (i * FACE_SIZE) + (i * PADDING),
                                                  yPos + FACE_Y,
                                                  table.getX() + (i * FACE_SIZE) + (i * PADDING) + FACE_SIZE,
                                                  yPos + FACE_Y));
        shapeRenderer.end();
    }

    private void renderFaces() {
        int i = 0;
        for (String heroId : party.getAllHeroIds()) {
            SpriteConfig faceConfig = Utils.getResourceManager().getSpriteConfig(heroId);
            String path = faceConfig.getFacePath();
            int row = faceConfig.getRow() - 1;
            int col = faceConfig.getCol() - 1;

            Texture texture = Utils.getResourceManager().getTextureAsset(path);
            TextureRegion[][] splitOfEight = TextureRegion.split(texture, (int) FACE_SIZE, (int) FACE_SIZE);
            TextureRegion heroFace = splitOfEight[row][col];
            var image = new Image(heroFace);
            image.setColor(TRANSPARENT_FACES);
            image.setPosition((i * FACE_SIZE) + (i * PADDING), yPos + FACE_Y);
            table.addActor(image);
            i++;
        }
    }

    private void renderNames() {
        var labelStyle = new Label.LabelStyle(fontBig, TRANSPARENT_BLACK);
        int i = 0;
        for (String heroName : party.getAllHeroNames()) {
            var heroLabel = new Label(heroName, labelStyle);
            heroLabel.setPosition(i * FACE_SIZE + (i * PADDING) + PADDING_SMALL,
                                  yPos + FACE_Y - PADDING_NAME);
            table.addActor(heroLabel);
            i++;
        }
    }

    private void renderLevelLabels() {
        var labelStyle = new Label.LabelStyle(font, TRANSPARENT_BLACK);
        int i = 0;
        for (Integer level : party.getAllHeroLevels()) {
            var levelLabel = new Label("Level: " + level.toString(), labelStyle);
            levelLabel.setPosition(i * FACE_SIZE + (i * PADDING) + PADDING_SMALL,
                                   yPos + FACE_Y - (1f * LINE_HEIGHT) - PADDING_LEVEL);
            table.addActor(levelLabel);
            i++;
        }
    }

    private void renderHpLabels() {
        var labelStyle = new Label.LabelStyle(font, TRANSPARENT_BLACK);
        IntStream.rangeClosed(0, party.getSize() - 1)
                 .forEach(i -> {
                     var hpLabel = new Label("HP: ", labelStyle);
                     hpLabel.setPosition(i * FACE_SIZE + (i * PADDING) + PADDING_SMALL,
                                         yPos + FACE_Y - (2f * LINE_HEIGHT) - PADDING_LINE);
                     table.addActor(hpLabel);
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
                 .forEach(i -> {
                     var xpLabel = new Label("XP: ", labelStyle);
                     xpLabel.setPosition(i * FACE_SIZE + (i * PADDING) + PADDING_SMALL,
                                         yPos + FACE_Y - (3f * LINE_HEIGHT) - PADDING_LINE);
                     table.addActor(xpLabel);
                 });
    }

    private void renderXpBars() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.TAN);
        IntStream.rangeClosed(0, party.getSize() - 1)
                 .forEach(i -> renderBar(i, 3f, calculateXpBarWidth(i)));
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
                 .forEach(i -> renderBar(i, linePosition, BAR_WIDTH));
        shapeRenderer.end();
    }

    private void renderBar(int partyNumber, float linePosition, float barWidth) {
        shapeRenderer.rect(table.getX() + (partyNumber * FACE_SIZE) + (partyNumber * PADDING) + BAR_X,
                           yPos + FACE_Y - (linePosition * LINE_HEIGHT) + BAR_Y,
                           barWidth,
                           BAR_HEIGHT);
    }

}
