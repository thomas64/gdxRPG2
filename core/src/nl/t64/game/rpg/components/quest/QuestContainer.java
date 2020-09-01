package nl.t64.game.rpg.components.quest;

import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class QuestContainer {

    private static final String QUEST_CONFIGS = "configs/quests/";
    private static final String FILE_LIST = QUEST_CONFIGS + "_files.txt";

    private final Map<String, QuestGraph> quests;

    public QuestContainer() {
        this.quests = new HashMap<>();
        this.loadQuests();
    }

    public QuestGraph getQuestById(String questId) {
        return quests.get(questId);
    }

    public StateContainer createStateContainer() {
        var stateContainer = new StateContainer();
        quests.forEach((id, graph) -> stateContainer.setState(id, graph.currentState));
        return stateContainer;
    }

    public void setCurrentStates(StateContainer stateContainer) {
        quests.forEach((id, graph) -> graph.currentState = stateContainer.getState(id));
    }

    private void loadQuests() {
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        Arrays.stream(configFiles)
              .map(filePath -> Gdx.files.local(QUEST_CONFIGS + filePath).readString())
              .map(json -> Utils.readValue(json, QuestGraph.class))
              .forEach(quests::putAll);
        quests.forEach((questId, quest) -> quest.id = questId);
    }

}
