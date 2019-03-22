package nl.t64.game.rpg;

import lombok.Getter;
import nl.t64.game.rpg.components.party.HeroContainer;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.interactors.PartyManager;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;


@Getter
public class Data implements ProfileObserver {

    private HeroContainer heroes;
    private PartyContainer party;

    @Override
    public void onNotifyCreate(ProfileManager profileManager) {
        heroes = new HeroContainer();
        party = new PartyContainer();
        var partyManager = new PartyManager(this);
        partyManager.addFirstHero();
        onNotifySave(profileManager);
    }

    @Override
    public void onNotifySave(ProfileManager profileManager) {
        profileManager.setProperty("heroes", heroes);
        profileManager.setProperty("party", party);
    }

    @Override
    public void onNotifyLoad(ProfileManager profileManager) {
        heroes = profileManager.getProperty("heroes", HeroContainer.class);
        party = profileManager.getProperty("party", PartyContainer.class);
    }

}
