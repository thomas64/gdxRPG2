package nl.t64.game.rpg;

import lombok.Getter;
import nl.t64.game.rpg.components.party.HeroContainer;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.constants.Constant;
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
        addFirstHeroToParty();
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

    private void addFirstHeroToParty() {
        HeroItem hero = heroes.getHero(Constant.PLAYER_ID);
        heroes.removeHero(Constant.PLAYER_ID);
        try {
            party.addHero(hero);
        } catch (PartyContainer.FullException e) {
            throw new IllegalStateException();
        }
    }

}
