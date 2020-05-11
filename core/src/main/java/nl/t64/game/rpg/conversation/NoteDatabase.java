package nl.t64.game.rpg.conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;


public class NoteDatabase {

    private static final String NOTES_CONFIGS = "configs/notes";
    private static final String SUFFIX = ".json";

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
        FileHandle[] configFiles = Gdx.files.local(NOTES_CONFIGS).list(SUFFIX);
        TypeReference<HashMap<String, ConversationGraph>> typeReference = new TypeReference<>() {
        };
        for (FileHandle file : configFiles) {
            String json = file.readString();
            notes.putAll(mapper.readValue(json, typeReference));
        }
    }

}
