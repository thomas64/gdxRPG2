package nl.t64.game.rpg.components.cutscene;

import java.util.HashMap;
import java.util.Map;


public class CutsceneContainer {

    private final Map<String, Boolean> cutscenes;

    public CutsceneContainer() {
        this.cutscenes = new HashMap<>(1);
        this.loadCutscenes();
    }

    public boolean isPlayed(String cutsceneId) {
        return cutscenes.get(cutsceneId);
    }

    public void setPlayed(String cutsceneId) {
        cutscenes.put(cutsceneId, true);
    }

    private void loadCutscenes() {
        cutscenes.put(CutsceneId.SCENE_INTRO, false);
    }

}
