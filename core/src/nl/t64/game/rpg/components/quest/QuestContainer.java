package nl.t64.game.rpg.components.quest;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;


public class QuestContainer {

    private static final String QUEST_CONFIGS = "configs/quests/";
    private static final String FILE_LIST = QUEST_CONFIGS + "_files.txt";

    private final Map<String, QuestGraph> quests;

    public QuestContainer() {
        this.quests = new HashMap<>();
        try {
            this.loadQuests();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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

    private void loadQuests() throws IOException {
        var mapper = new ObjectMapper();
        var typeReference = new TypeReference<HashMap<String, QuestGraph>>() {
        };
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        for (String filePath : configFiles) {
            String json = Gdx.files.local(QUEST_CONFIGS + filePath).readString();
            quests.putAll(mapper.readValue(json, typeReference));
        }
    }

}
