package nl.t64.game.rpg.profile;


public interface ProfileObserver {

    void onNotifyCreate(ProfileManager profileManager);

    void onNotifySave(ProfileManager profileManager);

    void onNotifyLoad(ProfileManager profileManager);

}
