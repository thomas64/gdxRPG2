package nl.t64.game.rpg.audio;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum AudioEvent {

    BGM_TITLE("audio/bgm/brave.mp3"),
    BGM_FOREST("audio/bgm/journey.ogg"),
    BGM_TOWN("audio/bgm/town.ogg"),
    BGM_INN("audio/bgm/store.ogg"),
    BGM_HOUSE("audio/bgm/wake_up.ogg"),
    BGM_CELLAR("audio/bgm/quiet_town.ogg"),
    BGM_SECRET_CHAMBER("audio/bgm/woods.ogg"),

    BGS_BIRDS("audio/bgs/birds.ogg"),
    BGS_CREEK("audio/bgs/creek.ogg"),
    BGS_FIREPLACE("audio/bgs/fireplace.ogg"),
    BGS_SPRING_FLOW("audio/bgs/spring_flow.ogg"),
    BGS_CROWS("audio/bgs/crows.ogg"),

    SE_MENU_CURSOR("audio/se/botw_menu_cursor.wav"),
    SE_MENU_CONFIRM("audio/se/botw_menu_confirm.wav"),
    SE_MENU_BACK("audio/se/botw_menu_back.wav"),
    SE_MENU_ERROR("audio/se/botw_menu_error.wav"),
    SE_MENU_TYPING("audio/se/botw_menu_typing.wav"),

    SE_CONVERSATION_START("audio/se/botw_conversation_start.ogg"),
    SE_CONVERSATION_NEXT("audio/se/botw_conversation_next.wav"),
    SE_CONVERSATION_END("audio/se/botw_conversation_end.wav"),
    SE_CONVERSATION_CURSOR("audio/se/botw_conversation_cursor.wav"),

    SE_SCROLL("audio/se/scroll.ogg"),
    SE_CHEST("audio/se/chest.ogg"),
    SE_SPARKLE("audio/se/sparkle.ogg"),
    SE_REWARD("audio/se/virix_reward.wav", 0.6f),
    SE_JOIN("audio/se/virix_join.wav", 0.5f),
    SE_LEVELUP("audio/se/copyc4t_levelup.ogg", 0.3f),
    SE_EQUIP("audio/se/kenney_cloth3.ogg"),
    SE_TAKE("audio/se/kenney_handle_small_leather2.ogg"),
    SE_COINS_BUY("audio/se/kenney_handle_coins.ogg"),
    SE_COINS_SELL("audio/se/kenney_handle_coins2.ogg"),
    SE_WARP("audio/se/whooshes_impacts_impact_01.ogg", 0.4f),

    NONE("");

    final String filePath;
    final float volume;

    AudioEvent(String filePath) {
        this(filePath, 1.0f);
    }

}
