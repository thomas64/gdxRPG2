package nl.t64.game.rpg.constants;


public class Constant {

    public static final int TILE_SIZE = 48;

    public static final float MOVE_SPEED_1 = 48f;  // = pixels/second
    public static final float MOVE_SPEED_2 = 144f; // 48 * 3
    public static final float MOVE_SPEED_3 = 240f; // 48 * 5
    public static final float MOVE_SPEED_4 = 960f; // 48 * 20

    private Constant() {
        throw new IllegalStateException("Constant class");
    }

}
