package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.screens.ScreenManager;
import nl.t64.game.rpg.screens.world.MapManager;

import java.util.Map;


public final class Utils {

    private static final float FACE_SIZE = 144f;

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

    public static Image getFaceImage(String heroId) {
        SpriteConfig faceConfig = getResourceManager().getSpriteConfig(heroId);
        String path = faceConfig.getFacePath();
        int row = faceConfig.getRow() - 1;
        int col = faceConfig.getCol() - 1;
        Texture texture = getResourceManager().getTextureAsset(path);
        TextureRegion[][] splitOfEight = TextureRegion.split(texture, (int) FACE_SIZE, (int) FACE_SIZE);
        TextureRegion heroFace = splitOfEight[row][col];
        return new Image(heroFace);
    }

    public static Color getHpColor(Map<String, Integer> hpStats) {
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

}
