package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.screens.ScreenManager;
import nl.t64.game.rpg.screens.world.MapManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public final class Utils {

    private static final String CHAR_PATH = "sprites/characters/%s.png";
    private static final String FACE_PATH = "sprites/faces/%s.png";

    private Utils() {
        throw new IllegalStateException("Utils class");
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

    public static TextureRegion[][] getCharImage(String spriteId) {
        SpriteConfig charConfig = getResourceManager().getSpriteConfig(spriteId);
        String path = String.format(CHAR_PATH, charConfig.getSource());
        int row = charConfig.getRow() - 1;
        int col = charConfig.getCol() - 1;
        Texture texture = Utils.getResourceManager().getTextureAsset(path);
        TextureRegion[][] splitOfEight = TextureRegion.split(texture,
                                                             Constant.SPRITE_GROUP_WIDTH,
                                                             Constant.SPRITE_GROUP_HEIGHT);
        TextureRegion personSprite = splitOfEight[row][col];
        return personSprite.split((int) Constant.TILE_SIZE, (int) Constant.TILE_SIZE);
    }

    public static Image getFaceImage(String spriteId) {
        SpriteConfig faceConfig = getResourceManager().getSpriteConfig(spriteId);
        String path = String.format(FACE_PATH, faceConfig.getSource());
        int row = faceConfig.getRow() - 1;
        int col = faceConfig.getCol() - 1;
        Texture texture = getResourceManager().getTextureAsset(path);
        TextureRegion[][] splitOfEight = TextureRegion.split(texture,
                                                             (int) Constant.FACE_SIZE,
                                                             (int) Constant.FACE_SIZE);
        TextureRegion characterFace = splitOfEight[row][col];
        return new Image(characterFace);
    }

    public static Color getHpColor(Map<String, Integer> hpStats) {
        Color color = Color.ROYAL;
        if (hpStats.get("lvlVari") < hpStats.get("lvlRank")) {
            color = Color.LIME;
        }
        if (hpStats.get("staVari") < hpStats.get("staRank")) {
            color = Color.YELLOW;
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
