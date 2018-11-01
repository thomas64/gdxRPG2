package nl.t64.game.rpg.profile;


public interface ProfileObserver {

    void onNotifyCreate();

    void onNotifySave();

    void onNotifyLoad();

}
