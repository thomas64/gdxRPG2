package nl.t64.game.rpg.constants

import com.badlogic.gdx.Screen
import nl.t64.game.rpg.screens.LoadScreen
import nl.t64.game.rpg.screens.battle.BattleScreen
import nl.t64.game.rpg.screens.cutscene.SceneIntro
import nl.t64.game.rpg.screens.inventory.InventoryScreen
import nl.t64.game.rpg.screens.loot.FindScreen
import nl.t64.game.rpg.screens.loot.ReceiveScreen
import nl.t64.game.rpg.screens.loot.RewardScreen
import nl.t64.game.rpg.screens.loot.SpoilsScreen
import nl.t64.game.rpg.screens.menu.*
import nl.t64.game.rpg.screens.questlog.QuestLogScreen
import nl.t64.game.rpg.screens.shop.ShopScreen
import nl.t64.game.rpg.screens.world.WorldScreen


enum class ScreenType(val screenClass: Class<out Screen>) {

    MENU_MAIN(MenuMain::class.java),
    MENU_NEW(MenuNew::class.java),
    MENU_LOAD(MenuLoad::class.java),
    MENU_SETTINGS(MenuSettings::class.java),
    MENU_CREDITS(MenuCredits::class.java),
    MENU_CONTROLS(MenuControls::class.java),
    MENU_PAUSE(MenuPause::class.java),
    WORLD(WorldScreen::class.java),
    BATTLE(BattleScreen::class.java),
    LOAD_SCREEN(LoadScreen::class.java),
    INVENTORY(InventoryScreen::class.java),
    QUEST_LOG(QuestLogScreen::class.java),
    SHOP(ShopScreen::class.java),
    FIND(FindScreen::class.java),
    REWARD(RewardScreen::class.java),
    RECEIVE(ReceiveScreen::class.java),
    SPOILS(SpoilsScreen::class.java),

    SCENE_INTRO(SceneIntro::class.java);

}
