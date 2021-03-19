package nl.t64.game.rpg.subjects;

import com.badlogic.gdx.graphics.Color;
import nl.t64.game.rpg.screens.world.GameMap;


public interface MapObserver {

    void onNotifyMapWillChange(Runnable changeMap, Color transitionColor);

    void onNotifyMapChanged(GameMap currentMap);

    void onNotifyShakeCamera();

}
