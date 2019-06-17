package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.SpriteConfig;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;

import java.util.Map;


class HeroesTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final String SPRITE_RIGHT_BORDER = "sprites/right_border.png";
    private static final String FONT_PATH = "fonts/spectral.ttf";
    private static final String FONT_BIG_PATH = "fonts/spectral_big.ttf";
    private static final int FONT_BIG_SIZE = 28;
    private static final int FONT_SIZE = 20;
    private static final float FACE_SIZE = 144f;
    private static final float STATS_COLUMN_PAD_LEFT = 10f;
    private static final float HP_LABEL_PAD_BOTTOM = 32f;
    private static final float STATS_COLUMN_WIDTH = FACE_SIZE;
    private static final float FULL_COLUMN_WIDTH = FACE_SIZE + STATS_COLUMN_PAD_LEFT + STATS_COLUMN_WIDTH + 1f;

    private static final float BAR_WIDTH = 136f;
    private static final float BAR_HEIGHT = 18f;

    final Table heroes;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final BitmapFont fontBig;
    private final Label.LabelStyle nameStyle;
    private final Label.LabelStyle levelStyle;
    private PartyContainer party;

    HeroesTable() {
        this.shapeRenderer = new ShapeRenderer();
        this.font = Utils.getResourceManager().getTrueTypeAsset(FONT_PATH, FONT_SIZE);
        this.fontBig = Utils.getResourceManager().getTrueTypeAsset(FONT_BIG_PATH, FONT_BIG_SIZE);
        this.nameStyle = new Label.LabelStyle(this.fontBig, Color.BLACK);
        this.levelStyle = new Label.LabelStyle(this.font, Color.BLACK);
        this.heroes = new Table();
        setTopBorder();
//        this.heroes.debugAll();
    }

    void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        fontBig.dispose();
    }

    void render() {
        party = Utils.getGameData().getParty();
        heroes.clear();
        for (HeroItem hero : party.getAllHeroes()) {
            addFace(hero);
            addStats(hero);
        }
    }

    private void addFace(HeroItem hero) {
        String heroId = party.getHeroId(hero);
        SpriteConfig faceConfig = Utils.getResourceManager().getSpriteConfig(heroId);
        String path = faceConfig.getFacePath();
        int row = faceConfig.getRow() - 1;
        int col = faceConfig.getCol() - 1;

        Texture texture = Utils.getResourceManager().getTextureAsset(path);
        TextureRegion[][] splitOfEight = TextureRegion.split(texture, (int) FACE_SIZE, (int) FACE_SIZE);
        TextureRegion heroFace = splitOfEight[row][col];
        var image = new Image(heroFace);
        heroes.add(image);
    }

    private void addStats(HeroItem hero) {
        var statsTable = new Table();
//        statsTable.debugAll();
        statsTable.defaults().align(Align.left);
        statsTable.columnDefaults(0).width(STATS_COLUMN_WIDTH);

        if (!hero.isLastInParty()) {
            setRightBorder(statsTable);
        }
        addLabels(statsTable, hero);
        heroes.add(statsTable).padLeft(STATS_COLUMN_PAD_LEFT).top();
        addHpBar(statsTable, hero);
    }

    private void addLabels(Table statsTable, HeroItem hero) {
        var nameLabel = new Label(hero.getName(), nameStyle);
        statsTable.add(nameLabel).row();
        var levelLabel = new Label("Level:   " + hero.getLevel(), levelStyle);
        statsTable.add(levelLabel).row();
        var hpLabel = new Label("HP:  " + hero.getCurrentHp() + "/ " + hero.getMaximumHp(), levelStyle);
        statsTable.add(hpLabel).padBottom(HP_LABEL_PAD_BOTTOM).row();
    }

    private void addHpBar(Table table, HeroItem hero) {
        int index = party.getIndex(hero);
        Vector2 tablePosition = table.localToStageCoordinates(new Vector2(table.getX(), table.getY()));
        float columnX = tablePosition.x + index * FULL_COLUMN_WIDTH;
        float columnY = tablePosition.y;

        renderBar(hero, columnX, columnY);
        renderBarOutline(columnX, columnY);
    }

    private void renderBar(HeroItem hero, float columnX, float columnY) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Map<String, Integer> hpStats = hero.getAllHpStats();
        Color color = getHpColor(hpStats);
        shapeRenderer.setColor(color);
        float barWidth = (BAR_WIDTH / hero.getMaximumHp()) * hero.getCurrentHp();
        shapeRenderer.rect(columnX + FACE_SIZE + STATS_COLUMN_PAD_LEFT,
                           columnY + STATS_COLUMN_PAD_LEFT,
                           barWidth, BAR_HEIGHT);
        shapeRenderer.end();
    }

    private void renderBarOutline(float columnX, float columnY) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(columnX + FACE_SIZE + STATS_COLUMN_PAD_LEFT,
                           columnY + STATS_COLUMN_PAD_LEFT,
                           BAR_WIDTH, BAR_HEIGHT);
        shapeRenderer.end();
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

    private void setTopBorder() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        heroes.setBackground(drawable);
    }

    private void setRightBorder(Table statsTable) {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_RIGHT_BORDER);
        var ninepatch = new NinePatch(texture, 0, 1, 0, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        statsTable.setBackground(drawable);
    }

}
