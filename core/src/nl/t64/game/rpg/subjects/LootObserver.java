package nl.t64.game.rpg.subjects;


public interface LootObserver {

    void onNotifyLootTaken();

    void onNotifyRewardTaken();

    void onNotifyReceiveTaken();

}
