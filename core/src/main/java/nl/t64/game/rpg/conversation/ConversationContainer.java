package nl.t64.game.rpg.conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;


public class ConversationContainer {

    private static final String CONVERSATION_CONFIGS = "configs/conversations";
    private static final String SUFFIX = ".json";

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
        final ConversationGraph graph = conversations.get(conversationId);
        // remove this single line below, and conversations will remember where they stopped the last time.
        graph.setCurrentPhraseId(ConversationGraph.DEFAULT_STARTING_PHRASE_ID);
        return graph;
    }

    public PhraseIdContainer getCurrentPhraseIds() {
        var currentPhraseIds = new PhraseIdContainer();
        conversations.forEach(
                (conversationId, graph) -> currentPhraseIds.setPhraseId(conversationId, graph.getCurrentPhraseId()));
        return currentPhraseIds;
    }

    public void setCurrentPhraseIds(PhraseIdContainer currentPhraseIds) {
        conversations.forEach((conversationId, graph) -> {
            String phraseId = currentPhraseIds.getPhraseId(conversationId);
            graph.setCurrentPhraseId(phraseId);
        });
    }

    private void loadConversations() throws IOException {
        var mapper = new ObjectMapper();
        FileHandle[] configFiles = Gdx.files.local(CONVERSATION_CONFIGS).list(SUFFIX);
        TypeReference<HashMap<String, ConversationGraph>> typeReference = new TypeReference<>() {
        };
        for (FileHandle file : configFiles) {
            String json = file.readString();
            conversations.putAll(mapper.readValue(json, typeReference));
        }
    }

}
