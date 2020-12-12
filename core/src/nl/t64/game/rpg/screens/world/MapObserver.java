package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;


interface MapObserver {

    void onNotifyMapWillChange(Runnable changeMap, Color transitionColor);

    void onNotifyMapChanged(GameMap currentMap);

    void onNotifyShakeCamera();

}
