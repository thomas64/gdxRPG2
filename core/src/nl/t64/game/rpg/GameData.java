package nl.t64.game.rpg;

import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.components.conversation.ConversationContainer;
import nl.t64.game.rpg.components.conversation.PhraseIdContainer;
import nl.t64.game.rpg.components.door.DoorContainer;
import nl.t64.game.rpg.components.loot.LootContainer;
import nl.t64.game.rpg.components.party.*;
import nl.t64.game.rpg.components.quest.QuestContainer;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;


@Getter
public class GameData implements ProfileObserver {

    private HeroContainer heroes;
    private PartyContainer party;
    private InventoryContainer inventory;
    private ConversationContainer conversations;
    private QuestContainer quests;
    private LootContainer loot;
    private DoorContainer doors;
    @Setter
    private boolean isFirstTimeLootMessageShown;
    @Setter
    private boolean isComparingEnabled;

    @Override
    public void onNotifyCreateProfile(ProfileManager profileManager) {
        heroes = new HeroContainer();
        party = new PartyContainer();
        inventory = new InventoryContainer();
        conversations = new ConversationContainer();
        quests = new QuestContainer();
        loot = new LootContainer();
        doors = new DoorContainer();
        isFirstTimeLootMessageShown = false;
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
        profileManager.setProperty("conversations", conversations.createPhraseIdContainer());
        profileManager.setProperty("quests", quests);
        profileManager.setProperty("loot", loot);
        profileManager.setProperty("doors", doors);
        profileManager.setProperty("isFirstTimeLootMessageShown", isFirstTimeLootMessageShown);
        profileManager.setProperty("isComparingEnabled", isComparingEnabled);
    }

    @Override
    public void onNotifyLoadProfile(ProfileManager profileManager) {
        heroes = profileManager.getProperty("heroes", HeroContainer.class);
        party = profileManager.getProperty("party", PartyContainer.class);
        inventory = profileManager.getProperty("inventory", InventoryContainer.class);
        conversations = new ConversationContainer();
        var currentPhraseIds = profileManager.getProperty("conversations", PhraseIdContainer.class);
        conversations.setCurrentPhraseIds(currentPhraseIds);
        quests = profileManager.getProperty("quests", QuestContainer.class);
        loot = profileManager.getProperty("loot", LootContainer.class);
        doors = profileManager.getProperty("doors", DoorContainer.class);
        isFirstTimeLootMessageShown = profileManager.getProperty("isFirstTimeLootMessageShown", Boolean.class);
        isComparingEnabled = profileManager.getProperty("isComparingEnabled", Boolean.class);
    }

    private void addFirstHeroToParty() {
        HeroItem hero = heroes.getHero(Constant.PLAYER_ID);
        heroes.removeHero(Constant.PLAYER_ID);
        party.addHero(hero);
    }

    private void addFirstItemsToInventory() {
        InventoryItem mace = InventoryDatabase.getInstance().createInventoryItem("basic_mace");
        InventoryItem gold = InventoryDatabase.getInstance().createInventoryItem("gold");
        inventory.autoSetItem(mace);
        inventory.autoSetItem(gold);
    }

}
