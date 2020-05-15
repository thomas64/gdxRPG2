package nl.t64.game.rpg.profile;


public interface ProfileObserver {

    void onNotifyCreateProfile(ProfileManager profileManager);

    void onNotifySaveProfile(ProfileManager profileManager);

    void onNotifyLoadProfile(ProfileManager profileManager);

}
