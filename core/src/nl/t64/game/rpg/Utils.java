package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.screens.ScreenManager;
import nl.t64.game.rpg.screens.world.MapManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public final class Utils {

    private static final float TITLE_PADDING = 50f;
    private static final String TITLE_FONT = "fonts/fff_tusj.ttf";
    private static final int TITLE_SIZE = 30;
    private static final String SPRITE_BORDER = "sprites/border.png";
    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final String CHAR_PATH = "sprites/characters/%s.png";
    private static final String FACE_PATH = "sprites/faces/%s.png";
    private static final String CHEST_PATH = "sprites/objects/chest.png";

    private Utils() {
        throw new IllegalCallerException("Utils class");
    }

    public static ResourceManager getResourceManager() {
        return getEngine().resourceManager;
    }

    public static Settings getSettings() {
        return getEngine().settings;
    }

    public static ProfileManager getProfileManager() {
        return getEngine().profileManager;
    }

    public static GameData getGameData() {
        return getEngine().gameData;
    }

    public static ScreenManager getScreenManager() {
        return getEngine().screenManager;
    }

    public static MapManager getMapManager() {
        return getEngine().mapManager;
    }

    private static Engine getEngine() {
        return (Engine) Gdx.app.getApplicationListener();
    }

    public static Window createDefaultWindow(String title, Table table) {
        var window = new Window(title, createDefaultWindowStyle());
        window.add(table);
        window.padTop(TITLE_PADDING);
        window.getTitleLabel().setAlignment(Align.left);
        window.pack();
        return window;
    }

    private static Window.WindowStyle createDefaultWindowStyle() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BORDER);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        var drawable = new NinePatchDrawable(ninepatch);
        BitmapFont font = Utils.getResourceManager().getTrueTypeAsset(TITLE_FONT, TITLE_SIZE);
        return new Window.WindowStyle(font, Color.BLACK, drawable);
    }

    public static Drawable createTopBorder() {
        var texture = getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        return new NinePatchDrawable(ninepatch);
    }

    public static TextureRegion[][] getCharImage(String spriteId) {
        SpriteConfig charConfig = getResourceManager().getSpriteConfig(spriteId);
        String path = String.format(CHAR_PATH, charConfig.getSource());
        int row = charConfig.getRow() - 1;
        int col = charConfig.getCol() - 1;
        TextureRegion[][] splitOfEight =
                getSplitTexture(path, Constant.SPRITE_GROUP_WIDTH, Constant.SPRITE_GROUP_HEIGHT);
        TextureRegion personSprite = splitOfEight[row][col];
        return personSprite.split((int) Constant.TILE_SIZE, (int) Constant.TILE_SIZE);
    }

    public static Image getFaceImage(String spriteId) {
        SpriteConfig faceConfig = getResourceManager().getSpriteConfig(spriteId);
        String path = String.format(FACE_PATH, faceConfig.getSource());
        int row = faceConfig.getRow() - 1;
        int col = faceConfig.getCol() - 1;
        TextureRegion[][] splitOfEight = getSplitTexture(path, (int) Constant.FACE_SIZE);
        TextureRegion characterFace = splitOfEight[row][col];
        return new Image(characterFace);
    }

    public static List<TextureRegion> getChestImage() {
        TextureRegion[][] splitOfEight =
                getSplitTexture(CHEST_PATH, Constant.SPRITE_GROUP_WIDTH, Constant.SPRITE_GROUP_HEIGHT);
        TextureRegion firstRedChest = splitOfEight[0][0];
        TextureRegion[][] twelveRedChestTextures = firstRedChest.split((int) Constant.TILE_SIZE,
                                                                       (int) Constant.TILE_SIZE);
        return List.of(twelveRedChestTextures[0][0],
                       twelveRedChestTextures[3][0]);
    }

    public static TextureRegion[][] getSplitTexture(String texturePath, int size) {
        return getSplitTexture(texturePath, size, size);
    }

    private static TextureRegion[][] getSplitTexture(String texturePath, int width, int height) {
        Texture completeTexture = getResourceManager().getTextureAsset(texturePath);
        return TextureRegion.split(completeTexture, width, height);
    }

    public static Color getHpColor(Map<String, Integer> hpStats) {
        Color color = Color.ROYAL;
        if (hpStats.get("lvlVari") < hpStats.get("lvlRank")) {
            color = Color.LIME;
        }
        if (hpStats.get("staVari") < hpStats.get("staRank")) {
            color = Color.GOLD;
        }
        if (hpStats.get("eduVari") < hpStats.get("eduRank")) {
            color = Color.FIREBRICK;
            if (hpStats.get("staVari") > 0) {
                color = Color.ORANGE;
            }
        }
        return color;
    }

    public static <T> List<T> reverseList(List<T> list) {
        List<T> reverse = new ArrayList<>(list);
        Collections.reverse(reverse);
        return reverse;
    }

}
