package nl.t64.game.rpg.components.conversation;

import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public final class NoteDatabase {

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
            loadNotes();
        }
        return notes.get(noteId);
    }

    private void loadNotes() {
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        Arrays.stream(configFiles)
              .map(filePath -> Gdx.files.local(NOTES_CONFIGS + filePath).readString())
              .map(json -> Utils.readValue(json, ConversationGraph.class))
              .forEach(notes::putAll);
    }

}
