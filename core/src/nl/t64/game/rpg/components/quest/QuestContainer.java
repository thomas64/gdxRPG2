package nl.t64.game.rpg.components.quest;

import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.Utils;

import java.util.*;


public class QuestContainer {

    private static final String QUEST_CONFIGS = "configs/quests/";
    private static final String FILE_LIST = QUEST_CONFIGS + "_files.txt";

    private final Map<String, QuestGraph> quests;

    public QuestContainer() {
        this.quests = new HashMap<>();
        this.loadQuests();
    }

    public List<QuestGraph> getAllKnownQuests() {
        return quests.values().stream()
                     .filter(questGraph -> !questGraph.currentState.equals(QuestState.UNKNOWN))
                     .sorted(Comparator.comparing((QuestGraph questGraph) -> questGraph.isFailed)
                                       .thenComparing((QuestGraph questGraph) -> questGraph.currentState)
                                       .thenComparing((QuestGraph questGraph) -> questGraph.id))
                     .toList();
    }

    public QuestGraph getQuestById(String questId) {
        return quests.get(questId);
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
