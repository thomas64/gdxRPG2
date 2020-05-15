package nl.t64.game.rpg.conversation;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;


public class NoteDatabase {

    private static final String NOTES_CONFIGS = "configs/notes/";
    private static final String FILE_LIST = NOTES_CONFIGS + "_files.txt";

    private static NoteDatabase instance;

    private final Map<String, ConversationGraph> notes;

    private NoteDatabase() {
        this.notes = new HashMap<>();
    }

    public static NoteDatabase getInstance() {
        if (instance == null) {
            instance = new NoteDatabase();
        }
        return instance;
    }

    public ConversationGraph getNoteById(String noteId) {
        if (notes.isEmpty()) {
            try {
                loadNotes();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return notes.get(noteId);
    }

    private void loadNotes() throws IOException {
        var mapper = new ObjectMapper();
        var typeReference = new TypeReference<HashMap<String, ConversationGraph>>() {
        };
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        for (String filePath : configFiles) {
            String json = Gdx.files.local(NOTES_CONFIGS + filePath).readString();
            notes.putAll(mapper.readValue(json, typeReference));
        }
    }

}
