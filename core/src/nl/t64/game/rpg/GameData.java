package nl.t64.game.rpg;

import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.components.battle.BattleContainer;
import nl.t64.game.rpg.components.conversation.ConversationContainer;
import nl.t64.game.rpg.components.conversation.PhraseIdContainer;
import nl.t64.game.rpg.components.cutscene.CutsceneContainer;
import nl.t64.game.rpg.components.door.DoorContainer;
import nl.t64.game.rpg.components.event.EventContainer;
import nl.t64.game.rpg.components.loot.LootContainer;
import nl.t64.game.rpg.components.party.*;
import nl.t64.game.rpg.components.quest.QuestContainer;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.subjects.ProfileObserver;


@Getter
public class GameData implements ProfileObserver {

    private HeroContainer heroes;
    private PartyContainer party;
    private InventoryContainer inventory;
    private BattleContainer battles;
    private ConversationContainer conversations;
    private QuestContainer quests;
    private EventContainer events;
    private LootContainer loot;
    private DoorContainer doors;
    private CutsceneContainer cutscenes;
    @Setter
    private boolean isTooltipEnabled;
    @Setter
    private boolean isComparingEnabled;

    @Override
    public void onNotifyCreateProfile(ProfileManager profileManager) {
        heroes = new HeroContainer();
        party = new PartyContainer();
        inventory = new InventoryContainer();
        battles = new BattleContainer();
        conversations = new ConversationContainer();
        quests = new QuestContainer();
        events = new EventContainer();
        loot = new LootContainer();
        doors = new DoorContainer();
        cutscenes = new CutsceneContainer();
        isTooltipEnabled = true;
        isComparingEnabled = true;
        addFirstHeroToParty();
        addFirstItemsToInventory();
        onNotifySaveProfile(profileManager);
    }

    @Override
    public void onNotifySaveProfile(ProfileManager profileManager) {
        profileManager.setProperty("heroes", heroes);
        profileManager.setProperty("party", party);
        profileManager.setProperty("inventory", inventory);
        profileManager.setProperty("battles", battles);
        profileManager.setProperty("conversations", conversations.createPhraseIdContainer());
        profileManager.setProperty("quests", quests);
        profileManager.setProperty("events", events);
        profileManager.setProperty("loot", loot);
        profileManager.setProperty("doors", doors);
        profileManager.setProperty("cutscenes", cutscenes);
        profileManager.setProperty("isTooltipEnabled", isTooltipEnabled);
        profileManager.setProperty("isComparingEnabled", isComparingEnabled);
    }

    @Override
    public void onNotifyLoadProfile(ProfileManager profileManager) {
        heroes = profileManager.getProperty("heroes", HeroContainer.class);
        party = profileManager.getProperty("party", PartyContainer.class);
        inventory = profileManager.getProperty("inventory", InventoryContainer.class);
        battles = profileManager.getProperty("battles", BattleContainer.class);
        conversations = new ConversationContainer();
        var currentPhraseIds = profileManager.getProperty("conversations", PhraseIdContainer.class);
        conversations.setCurrentPhraseIds(currentPhraseIds);
        quests = profileManager.getProperty("quests", QuestContainer.class);
        events = profileManager.getProperty("events", EventContainer.class);
        loot = profileManager.getProperty("loot", LootContainer.class);
        doors = profileManager.getProperty("doors", DoorContainer.class);
        cutscenes = profileManager.getProperty("cutscenes", CutsceneContainer.class);
        isTooltipEnabled = profileManager.getProperty("isTooltipEnabled", Boolean.class);
        isComparingEnabled = profileManager.getProperty("isComparingEnabled", Boolean.class);
    }

    private void addFirstHeroToParty() {
        HeroItem hero = heroes.getHero(Constant.PLAYER_ID);
        heroes.removeHero(Constant.PLAYER_ID);
        party.addHero(hero);
    }

    private void addFirstItemsToInventory() {
        InventoryItem mace = InventoryDatabase.createInventoryItem("basic_mace");
        InventoryItem gold = InventoryDatabase.createInventoryItem("gold");
        inventory.autoSetItem(mace);
        inventory.autoSetItem(gold);
    }

}
