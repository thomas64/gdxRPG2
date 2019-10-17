package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;

import java.util.Map;
import java.util.stream.IntStream;


class PartyWindow {

    private static final String FONT_PATH = "fonts/spectral.ttf";
    private static final String FONT_BIG_PATH = "fonts/spectral_big.ttf";
    private static final Color TRANSPARENT_BLACK = new Color(0f, 0f, 0f, 0.8f);
    private static final Color TRANSPARENT_WHITE = new Color(1f, 1f, 1f, 0.3f);
    private static final Color TRANSPARENT_FACES = new Color(1f, 1f, 1f, 0.7f);

    private static final int FONT_BIG_SIZE = 28;
    private static final int FONT_SIZE = 20;

    private static final float LINE_HEIGHT = 22f;
    private static final float FACE_SIZE = 144f;
    private static final float PADDING = 10f;
    private static final float PADDING_SMALL = 5f;
    private static final float PADDING_NAME = 7f;
    private static final float PADDING_LEVEL = 12f;
    private static final float PADDING_LINE = PADDING + PADDING_SMALL;
    private static final float FACE_Y = 90f;
    private static final float TABLE_WIDTH =
            (FACE_SIZE * PartyContainer.MAXIMUM) + (PADDING * (PartyContainer.MAXIMUM - 1f));
    private static final float TABLE_HEIGHT = FACE_Y + FACE_SIZE;
    private static final float HIGH_Y = 0f;
    private static final float LOW_Y = -TABLE_HEIGHT;

    private static final float BAR_X = 50f;
    private static final float BAR_Y = -4f;
    private static final float BAR_WIDTH = 85f;
    private static final float BAR_HEIGHT = 12f;

    private static final float VELOCITY = 800f;

    private PartyContainer party;
    private final Stage stage;
    private final Table table;
    private final BitmapFont font;
    private final BitmapFont fontBig;
    private final ShapeRenderer shapeRenderer;

    private float yPos;
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;


    PartyWindow() {
        this.font = Utils.getResourceManager().getTrueTypeAsset(FONT_PATH, FONT_SIZE);
        this.fontBig = Utils.getResourceManager().getTrueTypeAsset(FONT_BIG_PATH, FONT_BIG_SIZE);
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

    void update(float dt) {
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

    private void renderTable() {
        party = Utils.getGameData().getParty(); // todo, hij hoeft de party niet bij elke fps op te halen.
        table.clear();
        renderBackgrounds();
        renderSquares();
        IntStream.range(0, party.getSize())
                 .forEach(i -> {
                     renderHorizontalLine(i);
                     renderFace(i);
                     renderName(i);
                     renderLevelLabel(i);
                     renderHpLabel(i);
                     renderHpBar(i);
                     renderXpLabel(i);
                     renderXpBar(i);
                 });
    }

    private void renderBackgrounds() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(TRANSPARENT_WHITE);
        IntStream.range(0, party.getSize())
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
        IntStream.range(0, PartyContainer.MAXIMUM)
                 .forEach(i -> shapeRenderer.rect(table.getX() + (i * FACE_SIZE) + (i * PADDING),
                                                  yPos + PADDING,
                                                  FACE_SIZE,
                                                  TABLE_HEIGHT - PADDING));
        shapeRenderer.end();
    }

    private void renderHorizontalLine(int i) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(TRANSPARENT_BLACK);
        shapeRenderer.line(table.getX() + (i * FACE_SIZE) + (i * PADDING),
                           yPos + FACE_Y,
                           table.getX() + (i * FACE_SIZE) + (i * PADDING) + FACE_SIZE,
                           yPos + FACE_Y);
        shapeRenderer.end();
    }

    private void renderFace(int i) {
        String heroId = party.getHero(i).getId();
        Image image = Utils.getFaceImage(heroId);
        image.setColor(TRANSPARENT_FACES);
        image.setPosition((i * FACE_SIZE) + (i * PADDING), yPos + FACE_Y);
        table.addActor(image);
    }

    private void renderName(int i) {
        var labelStyle = new Label.LabelStyle(fontBig, TRANSPARENT_BLACK);
        String heroName = party.getHero(i).getName();
        var heroLabel = new Label(heroName, labelStyle);
        heroLabel.setPosition(i * FACE_SIZE + (i * PADDING) + PADDING_SMALL,
                              yPos + FACE_Y - PADDING_NAME);
        table.addActor(heroLabel);
    }

    private void renderLevelLabel(int i) {
        var labelStyle = new Label.LabelStyle(font, TRANSPARENT_BLACK);
        int heroLevel = party.getHero(i).getLevel();
        var levelLabel = new Label("Level: " + heroLevel, labelStyle);
        levelLabel.setPosition(i * FACE_SIZE + (i * PADDING) + PADDING_SMALL,
                               yPos + FACE_Y - (1f * LINE_HEIGHT) - PADDING_LEVEL);
        table.addActor(levelLabel);
    }

    private void renderHpLabel(int i) {
        var labelStyle = new Label.LabelStyle(font, TRANSPARENT_BLACK);
        var hpLabel = new Label("HP: ", labelStyle);
        hpLabel.setPosition(i * FACE_SIZE + (i * PADDING) + PADDING_SMALL,
                            yPos + FACE_Y - (2f * LINE_HEIGHT) - PADDING_LINE);
        table.addActor(hpLabel);
    }

    private void renderHpBar(int i) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        HeroItem hero = party.getHero(i);
        Map<String, Integer> hpStats = hero.getAllHpStats();
        Color color = Utils.getHpColor(hpStats);
        shapeRenderer.setColor(color);
        float barWidth = (BAR_WIDTH / hero.getMaximumHp()) * hero.getCurrentHp();
        renderBar(i, 2f, barWidth);
        shapeRenderer.end();

        renderBarOutline(i, 2f);
    }

    private void renderXpLabel(int i) {
        var labelStyle = new Label.LabelStyle(font, TRANSPARENT_BLACK);
        var xpLabel = new Label("XP: ", labelStyle);
        xpLabel.setPosition(i * FACE_SIZE + (i * PADDING) + PADDING_SMALL,
                            yPos + FACE_Y - (3f * LINE_HEIGHT) - PADDING_LINE);
        table.addActor(xpLabel);
    }

    private void renderXpBar(int i) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.TAN);
        renderBar(i, 3f, calculateXpBarWidth(i));
        shapeRenderer.end();

        renderBarOutline(i, 3f);
    }

    private float calculateXpBarWidth(int i) {
        HeroItem hero = party.getHero(i);
        int maxXp = hero.getXpDeltaBetweenLevels();
        int currentXp = maxXp - hero.getXpNeededForNextLevel();
        return (BAR_WIDTH / maxXp) * currentXp;
    }

    private void renderBarOutline(int i, float linePosition) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(TRANSPARENT_BLACK);
        renderBar(i, linePosition, BAR_WIDTH);
        shapeRenderer.end();
    }

    private void renderBar(int partyNumber, float linePosition, float barWidth) {
        shapeRenderer.rect(table.getX() + (partyNumber * FACE_SIZE) + (partyNumber * PADDING) + BAR_X,
                           yPos + FACE_Y - (linePosition * LINE_HEIGHT) + BAR_Y,
                           barWidth,
                           BAR_HEIGHT);
    }

}
