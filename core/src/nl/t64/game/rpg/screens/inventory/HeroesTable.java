package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;


public class HeroesTable {

    private static final String SPRITE_BORDER = "sprites/border.png";
    private static final String SPRITE_RIGHT_BORDER = "sprites/right_border.png";
    private static final String SPRITE_GRAY = "sprites/gray.png";
    private static final String FONT_PATH = "fonts/spectral_extra_bold_20.ttf";
    private static final String FONT_BIG_PATH = "fonts/spectral_extra_bold_28.ttf";
    private static final int FONT_BIG_SIZE = 28;
    private static final int FONT_SIZE = 20;
    private static final float STATS_COLUMN_WIDTH = 134f;
    private static final float STATS_COLUMN_PAD = 10f;
    private static final float BAR_ROW_HEIGHT = 32f;
    private static final float BAR_HEIGHT = 18f;
    private static final float BAR_PAD_TOP = 7f;

    public final Table heroes;
    private final Label.LabelStyle nameStyle;
    private final Label.LabelStyle levelStyle;
    private final PartyContainer party;
    private final Texture[] colorsOfHpBars;

    public HeroesTable() {
        this.party = Utils.getGameData().getParty();
        BitmapFont font = Utils.getResourceManager().getTrueTypeAsset(FONT_PATH, FONT_SIZE);
        BitmapFont fontBig = Utils.getResourceManager().getTrueTypeAsset(FONT_BIG_PATH, FONT_BIG_SIZE);
        this.nameStyle = new Label.LabelStyle(fontBig, Color.BLACK);
        this.levelStyle = new Label.LabelStyle(font, Color.BLACK);
        this.heroes = new Table();
        heroes.setBackground(Utils.createTopBorder());
        this.colorsOfHpBars = new Texture[PartyContainer.MAXIMUM];
    }

    public void update() {
        heroes.clear();
        for (HeroItem hero : party.getAllHeroes()) {
            createFace(hero);
            createStats(hero);
        }
    }

    private void createFace(HeroItem hero) {
        Stack stack = new Stack();
        addPossibleGrayBackgroundTo(stack, hero);
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
        addNameLabelTo(statsTable, hero);
        addLevelLabelTo(statsTable, hero);
        addHpLabelTo(statsTable, hero);
        addHpBarTo(statsTable, hero);

        Stack stack = new Stack();
        addPossibleGrayBackgroundTo(stack, hero);
        stack.add(statsTable);
        heroes.add(stack);
    }

    private Table createStatsTable(HeroItem hero) {
        var statsTable = new Table();
        statsTable.defaults().align(Align.left).top();
        statsTable.columnDefaults(0).width(STATS_COLUMN_PAD);
        statsTable.columnDefaults(1).width(STATS_COLUMN_WIDTH);
        statsTable.columnDefaults(2).width(STATS_COLUMN_PAD);
        if (!party.isHeroLast(hero)) {
            setRightBorder(statsTable);
        }
        return statsTable;
    }

    private void addPossibleGrayBackgroundTo(Stack stack, HeroItem hero) {
        if (hero.hasSameIdAs(InventoryUtils.getSelectedHero())) {
            Texture textureGray = Utils.getResourceManager().getTextureAsset(SPRITE_GRAY);
            Image imageGray = new Image(textureGray);
            stack.add(imageGray);
        }
    }

    private void addNameLabelTo(Table statsTable, HeroItem hero) {
        statsTable.add(new Label("", nameStyle));
        var nameLabel = new Label(hero.getName(), nameStyle);
        statsTable.add(nameLabel);
        statsTable.add(new Label("", nameStyle)).row();
    }

    private void addLevelLabelTo(Table statsTable, HeroItem hero) {
        statsTable.add(new Label("", levelStyle));
        var levelLabel = new Label("Level:   " + hero.getLevel(), levelStyle);
        statsTable.add(levelLabel);
        statsTable.add(new Label("", levelStyle)).row();
    }

    private void addHpLabelTo(Table statsTable, HeroItem hero) {
        statsTable.add(new Label("", levelStyle));
        var hpLabel = new Label("HP:  " + hero.getCurrentHp() + "/ " + hero.getMaximumHp(), levelStyle);
        statsTable.add(hpLabel);
        statsTable.add(new Label("", levelStyle));
    }

    private void addHpBarTo(Table statsTable, HeroItem hero) {
        statsTable.row().height(BAR_ROW_HEIGHT);
        statsTable.add(new Label("", levelStyle));
        statsTable.add(createHpBar(hero)).height(BAR_HEIGHT).padTop(BAR_PAD_TOP);
        statsTable.add(new Label("", levelStyle));
    }

    private void setRightBorder(Table statsTable) {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_RIGHT_BORDER);
        var ninepatch = new NinePatch(texture, 0, 1, 0, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        statsTable.setBackground(drawable);
    }

    private Stack createHpBar(HeroItem hero) {
        Image outline = createOutline();
        Image fill = createFill(hero);
        Stack stack = new Stack();
        stack.add(fill);
        stack.add(outline);
        return stack;
    }

    private Image createOutline() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BORDER);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        return new Image(ninepatch);
    }

    private Image createFill(HeroItem hero) {
        int index = party.getIndex(hero);
        if (colorsOfHpBars[index] != null) {
            colorsOfHpBars[index].dispose();
        }

        var pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        Map<String, Integer> hpStats = hero.getAllHpStats();
        Color color = Utils.getHpColor(hpStats);
        pixmap.setColor(color);
        pixmap.fill();
        colorsOfHpBars[index] = new Texture(pixmap);
        pixmap.dispose();
        var drawable = new TextureRegionDrawable(colorsOfHpBars[index]);
        float fillWidth = (STATS_COLUMN_WIDTH / hero.getMaximumHp()) * hero.getCurrentHp();
        drawable.setMinWidth(fillWidth);
        var image = new Image(drawable);
        image.setScaling(Scaling.stretchY);
        image.setAlign(Align.left);
        return image;
    }

    public void disposePixmapTextures() {
        Arrays.stream(colorsOfHpBars).filter(Objects::nonNull).forEach(Texture::dispose);
    }

}
