package nl.t64.game.rpg.components.conversation;

import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ConversationContainer {

    private static final String CONVERSATION_CONFIGS = "configs/conversations/";
    private static final String FILE_LIST = CONVERSATION_CONFIGS + "_files.txt";

    private final Map<String, ConversationGraph> conversations;

    public ConversationContainer() {
        this.conversations = new HashMap<>();
        this.loadConversations();
    }

    public ConversationGraph getConversationById(String conversationId) {
        return conversations.get(conversationId);
    }

    public PhraseIdContainer createPhraseIdContainer() {
        var phraseIdContainer = new PhraseIdContainer();
        conversations.forEach((id, graph) -> phraseIdContainer.setPhraseId(id, graph.currentPhraseId));
        return phraseIdContainer;
    }

    public void setCurrentPhraseIds(PhraseIdContainer phraseIdContainer) {
        conversations.forEach((id, graph) -> graph.currentPhraseId = phraseIdContainer.getPhraseId(id));
    }

    private void loadConversations() {
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        Arrays.stream(configFiles)
              .map(filePath -> Gdx.files.local(CONVERSATION_CONFIGS + filePath).readString())
              .map(json -> Utils.readValue(json, ConversationGraph.class))
              .forEach(conversations::putAll);
    }

}
