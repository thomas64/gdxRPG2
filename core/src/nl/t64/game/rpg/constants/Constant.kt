package nl.t64.game.rpg.constants

import com.badlogic.gdx.graphics.Color


object Constant {

    const val TITLE = "gdxRPG2"
    const val SCREEN_WIDTH = 1920
    const val SCREEN_HEIGHT = 1080
//    const val SCREEN_WIDTH = 1280
//    const val SCREEN_HEIGHT = 720

    const val TILE_SIZE = 48f
    const val HALF_TILE_SIZE = 24f
    const val FACE_SIZE = 144f
    const val SPRITE_GROUP_WIDTH = 144
    const val SPRITE_GROUP_HEIGHT = 192

    const val MOVE_SPEED_1 = 48f    // = pixels/second
    const val MOVE_SPEED_2 = 144f   // 48 * 3
    const val MOVE_SPEED_3 = 240f   // 48 * 5
    const val MOVE_SPEED_4 = 960f   // 48 * 20

    const val SLOW_FRAMES = 0.50f
    const val NORMAL_FRAMES = 0.25f
    const val FAST_FRAMES = 0.15f
    const val NO_FRAMES = 0f

    const val FADE_DURATION = 0.5f
    const val DIALOG_FADE_OUT_DURATION = 0.4f - 0.1f

    val DARK_RED = Color(0.5f, 0f, 0f, 1f)

    const val PLAYER_ID = "mozes"
    const val STARTING_MAP = "honeywood"

    const val DESCRIPTION_KEY_BUY = "Price"
    const val DESCRIPTION_KEY_BUY_PIECE = "Price per piece"
    const val DESCRIPTION_KEY_BUY_TOTAL = "Total price"
    const val DESCRIPTION_KEY_SELL = "Sell value"
    const val DESCRIPTION_KEY_SELL_PIECE = "Sell value per piece"
    const val DESCRIPTION_KEY_SELL_TOTAL = "Total sell value"

    const val PHRASE_ID_QUEST_ACCEPT = "1000"
    const val PHRASE_ID_QUEST_TOLERATE = "1500"
    const val PHRASE_ID_QUEST_IMMEDIATE_SUCCESS = "2000"
    const val PHRASE_ID_QUEST_SUCCESS = "2100"
    const val PHRASE_ID_QUEST_NO_SUCCESS = "2200"
    const val PHRASE_ID_QUEST_UNCLAIMED = "2300"
    const val PHRASE_ID_QUEST_FINISHED = "2400"
    const val PHRASE_ID_QUEST_DELIVERY = "2500"
    const val PHRASE_ID_QUEST_LINKED = "2600"
    const val PHRASE_ID_PARTY_FULL = "9000"

    const val KEYCODE_L1 = 1000
    const val KEYCODE_R1 = 1001
    const val KEYCODE_START = 1002
    const val KEYCODE_SELECT = 1003
    const val KEYCODE_TOP = 1004
    const val KEYCODE_LEFT = 1005
    const val KEYCODE_RIGHT = 1006
    const val KEYCODE_BOTTOM = 1007
    const val KEYCODE_L3 = 1008
    const val KEYCODE_R3 = 1009
    const val KEYCODE_R3_L = 1010
    const val KEYCODE_R3_R = 1011

}
