package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;

import java.util.Map;


class HeroesTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final String SPRITE_RIGHT_BORDER = "sprites/right_border.png";
    private static final String SPRITE_GREY = "sprites/grey.png";
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
    private final Label.LabelStyle nameStyle;
    private final Label.LabelStyle levelStyle;
    private final PartyContainer party;

    HeroesTable() {
        this.party = Utils.getGameData().getParty();
        this.shapeRenderer = new ShapeRenderer();
        BitmapFont font = Utils.getResourceManager().getTrueTypeAsset(FONT_PATH, FONT_SIZE);
        BitmapFont fontBig = Utils.getResourceManager().getTrueTypeAsset(FONT_BIG_PATH, FONT_BIG_SIZE);
        this.nameStyle = new Label.LabelStyle(fontBig, Color.BLACK);
        this.levelStyle = new Label.LabelStyle(font, Color.BLACK);
        this.heroes = new Table();
        setTopBorder();
    }

    void dispose() {
        shapeRenderer.dispose();
    }

    @SuppressWarnings("LibGDXFlushInsideLoop")
    void render() {
        heroes.clear();
        for (HeroItem hero : party.getAllHeroes()) {
            createFace(hero);
            createStats(hero);
        }
    }

    private void createFace(HeroItem hero) {
        Stack stack = new Stack();
        stack.addListener(new HeroesTableSelectListener(InventoryUtils::updateSelectedHero, hero));
        addPossibleGreyBackgroundTo(stack, hero);
        addFaceImageTo(stack, hero);
        heroes.add(stack);
    }

    private void addFaceImageTo(Stack stack, HeroItem hero) {
        String heroId = hero.getId();
        Image faceImage = Utils.getFaceImage(heroId);
        stack.add(faceImage);
    }

    private void createStats(HeroItem hero) {
        Table statsTable = createStatsTable(hero);
        Stack stack = new Stack();
        stack.addListener(new HeroesTableSelectListener(InventoryUtils::updateSelectedHero, hero));
        addPossibleGreyBackgroundTo(stack, hero);
        addLabelsTo(statsTable, hero);
        stack.add(statsTable);
        heroes.add(stack);
        addHpBarTo(statsTable, hero);
    }

    private Table createStatsTable(HeroItem hero) {
        var statsTable = new Table();
        statsTable.defaults().align(Align.left).top();
        statsTable.columnDefaults(0).width(STATS_COLUMN_PAD_LEFT);
        statsTable.columnDefaults(1).width(STATS_COLUMN_WIDTH);
        if (!party.isHeroLast(hero)) {
            setRightBorder(statsTable);
        }
        return statsTable;
    }

    private void addPossibleGreyBackgroundTo(Stack stack, HeroItem hero) {
        if (hero.equalsHero(InventoryUtils.selectedHero)) {
            Texture textureGrey = Utils.getResourceManager().getTextureAsset(SPRITE_GREY);
            Image imageGrey = new Image(textureGrey);
            stack.add(imageGrey);
        }
    }

    private void addLabelsTo(Table statsTable, HeroItem hero) {
        statsTable.add(new Label("", nameStyle));
        var nameLabel = new Label(hero.getName(), nameStyle);
        statsTable.add(nameLabel).row();
        statsTable.add(new Label("", levelStyle));
        var levelLabel = new Label("Level:   " + hero.getLevel(), levelStyle);
        statsTable.add(levelLabel).row();
        statsTable.add(new Label("", levelStyle));
        var hpLabel = new Label("HP:  " + hero.getCurrentHp() + "/ " + hero.getMaximumHp(), levelStyle);
        statsTable.add(hpLabel).row();
        statsTable.add(new Label("", levelStyle)).height(HP_LABEL_PAD_BOTTOM);
        statsTable.add(new Label("", levelStyle)).height(HP_LABEL_PAD_BOTTOM);
    }

    private void addHpBarTo(Table table, HeroItem hero) {
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
        Color color = Utils.getHpColor(hpStats);
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
