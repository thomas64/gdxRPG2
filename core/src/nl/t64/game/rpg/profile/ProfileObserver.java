package nl.t64.game.rpg.profile;

import nl.t64.game.rpg.constants.ProfileEvent;


public interface ProfileObserver {

    void onNotify(ProfileManager manager, ProfileEvent event);

}
