package nl.t64.game.rpg.managers;


public interface ProfileObserver {

    void onNotifyCreate();

    void onNotifySave();

    void onNotifyLoad();

}
