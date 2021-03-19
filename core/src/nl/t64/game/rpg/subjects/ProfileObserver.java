package nl.t64.game.rpg.subjects;

import nl.t64.game.rpg.ProfileManager;


public interface ProfileObserver {

    void onNotifyCreateProfile(ProfileManager profileManager);

    void onNotifySaveProfile(ProfileManager profileManager);

    void onNotifyLoadProfile(ProfileManager profileManager);

}
