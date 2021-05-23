package nl.t64.game.rpg.subjects;

import com.badlogic.gdx.math.Circle;


public interface DetectionObserver {

    void onNotifyDetection(Circle detectionRange);

}
