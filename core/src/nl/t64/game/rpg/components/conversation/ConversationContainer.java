package nl.t64.game.rpg.components.conversation;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;


public class ConversationContainer {

    private static final String CONVERSATION_CONFIGS = "configs/conversations/";
    private static final String FILE_LIST = CONVERSATION_CONFIGS + "_files.txt";

    private final Map<String, ConversationGraph> conversations;

    public ConversationContainer() {
        this.conversations = new HashMap<>();
        try {
            this.loadConversations();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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

    private void loadConversations() throws IOException {
        var mapper = new ObjectMapper();
        var typeReference = new TypeReference<HashMap<String, ConversationGraph>>() {
        };
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        for (String filePath : configFiles) {
            String json = Gdx.files.local(CONVERSATION_CONFIGS + filePath).readString();
            conversations.putAll(mapper.readValue(json, typeReference));
        }
    }

}
