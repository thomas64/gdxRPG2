package nl.t64.game.rpg.constants;

import com.badlogic.gdx.graphics.Color;


public final class Constant {

    public static final String TITLE = "gdxRPG2";
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;
//    public static final int SCREEN_WIDTH = 1280;
//    public static final int SCREEN_HEIGHT = 720;

    public static final float TILE_SIZE = 48f;
    public static final float FACE_SIZE = 144f;
    public static final int SPRITE_GROUP_WIDTH = 144;
    public static final int SPRITE_GROUP_HEIGHT = 192;

    public static final float MOVE_SPEED_1 = 48f;  // = pixels/second
    public static final float MOVE_SPEED_2 = 144f; // 48 * 3
    public static final float MOVE_SPEED_3 = 240f; // 48 * 5
    public static final float MOVE_SPEED_4 = 960f; // 48 * 20

    public static final float SLOW_FRAMES = 0.50f;
    public static final float NORMAL_FRAMES = 0.25f;
    public static final float FAST_FRAMES = 0.15f;
    public static final float NO_FRAMES = 0f;

    public static final Color DARK_RED = new Color(0.5f, 0f, 0f, 1f);

    public static final String PLAYER_ID = "mozes";
    public static final String STARTING_MAP = "starter_area";

    public static final String DESCRIPTION_KEY_BUY = "Price";
    public static final String DESCRIPTION_KEY_SELL = "Value";

    private Constant() {
        throw new IllegalStateException("Constant class");
    }

}
