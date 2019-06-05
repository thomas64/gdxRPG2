package nl.t64.game.rpg;

import lombok.Getter;
import nl.t64.game.rpg.components.inventory.GlobalContainer;
import nl.t64.game.rpg.components.inventory.InventoryDatabase;
import nl.t64.game.rpg.components.inventory.InventoryItem;
import nl.t64.game.rpg.components.party.HeroContainer;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;


@Getter
public class GameData implements ProfileObserver {

    private HeroContainer heroes;
    private PartyContainer party;
    private GlobalContainer inventory;

    @Override
    public void onNotifyCreate(ProfileManager profileManager) {
        heroes = new HeroContainer();
        party = new PartyContainer();
        inventory = new GlobalContainer();
        addFirstHeroToParty();
        addFirstItemsToInventory();
        onNotifySave(profileManager);
    }

    @Override
    public void onNotifySave(ProfileManager profileManager) {
        profileManager.setProperty("heroes", heroes);
        profileManager.setProperty("party", party);
        profileManager.setProperty("inventory", inventory);
    }

    @Override
    public void onNotifyLoad(ProfileManager profileManager) {
        heroes = profileManager.getProperty("heroes", HeroContainer.class);
        party = profileManager.getProperty("party", PartyContainer.class);
        inventory = profileManager.getProperty("inventory", GlobalContainer.class);
    }

    private void addFirstHeroToParty() {
        HeroItem hero = heroes.getHero(Constant.PLAYER_ID);
        heroes.removeHero(Constant.PLAYER_ID);
        party.addHero(hero);
    }

    private void addFirstItemsToInventory() {
        InventoryItem gold = InventoryDatabase.getInstance().getInventoryItem("gold");
        InventoryItem mace = InventoryDatabase.getInstance().getInventoryItem("basic_mace");
        inventory.forceSetItemAt(0, gold);
        inventory.forceSetItemAt(1, mace);
    }

}
