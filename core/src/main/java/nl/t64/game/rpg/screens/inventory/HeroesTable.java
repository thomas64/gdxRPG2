package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.SpriteConfig;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;


class HeroesTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final String SPRITE_RIGHT_BORDER = "sprites/right_border.png";
    private static final String FONT_PATH = "fonts/spectral.ttf";
    private static final String FONT_BIG_PATH = "fonts/spectral_big.ttf";
    private static final int FONT_BIG_SIZE = 28;
    private static final int FONT_SIZE = 20;
    private static final float FACE_SIZE = 144f;
    private static final float FACE_PAD_RIGHT = 10f;
    final Table heroes;
    private final BitmapFont font;
    private final BitmapFont fontBig;
    private final Label.LabelStyle nameStyle;
    private final Label.LabelStyle levelStyle;
    private PartyContainer party;

    HeroesTable() {
        this.font = Utils.getResourceManager().getTrueTypeAsset(FONT_PATH, FONT_SIZE);
        this.fontBig = Utils.getResourceManager().getTrueTypeAsset(FONT_BIG_PATH, FONT_BIG_SIZE);
        this.nameStyle = new Label.LabelStyle(this.fontBig, Color.BLACK);
        this.levelStyle = new Label.LabelStyle(this.font, Color.BLACK);
        this.heroes = new Table();

        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        this.heroes.setBackground(drawable);
//        this.heroes.debugAll();
    }

    void render() {
        party = Utils.getGameData().getParty();
        heroes.clear();

        for (HeroItem hero : party.getAllHeroes()) {
            String heroId = party.getHeroId(hero);
            addFace(heroId);

            var table = new Table();
            table.debugAll();
            table.defaults().align(Align.left);
            table.columnDefaults(0).width(FACE_SIZE);
//            table.setHeight(FACE_SIZE);
            if (!hero.isLastInParty()) {
                var texture = Utils.getResourceManager().getTextureAsset(SPRITE_RIGHT_BORDER);
                var ninepatch = new NinePatch(texture, 0, 1, 0, 0);
                var drawable = new NinePatchDrawable(ninepatch);
                table.setBackground(drawable);
            }

            var nameLabel = new Label(hero.getName(), nameStyle);
            table.add(nameLabel).row();
            var levelLabel = new Label("Level: " + hero.getLevel(), levelStyle);
            table.add(levelLabel).row();
            var hpLabel = new Label("HP: " + hero.getCurrentHp() + "/ " + hero.getMaximumHp(), levelStyle);
            table.add(hpLabel).row();
            table.pack();
            heroes.add(table);

        }

    }

    private void addFace(String heroId) {
        SpriteConfig faceConfig = Utils.getResourceManager().getSpriteConfig(heroId);
        String path = faceConfig.getFacePath();
        int row = faceConfig.getRow() - 1;
        int col = faceConfig.getCol() - 1;

        Texture texture = Utils.getResourceManager().getTextureAsset(path);
        TextureRegion[][] splitOfEight = TextureRegion.split(texture, (int) FACE_SIZE, (int) FACE_SIZE);
        TextureRegion heroFace = splitOfEight[row][col];
        var image = new Image(heroFace);
        heroes.add(image).padRight(FACE_PAD_RIGHT);
    }

}
