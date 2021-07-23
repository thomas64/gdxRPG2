package nl.t64.game.rpg.components.conversation

import com.badlogic.gdx.Gdx
import nl.t64.game.rpg.Utils


private const val NOTES_CONFIGS = "configs/notes/"
private const val FILE_LIST = NOTES_CONFIGS + "_files.txt"

object NoteDatabase {

    private val notes: Map<String, ConversationGraph> = Gdx.files.internal(FILE_LIST).readString()
        .split(System.lineSeparator())
        .map { Gdx.files.internal(NOTES_CONFIGS + it).readString() }
        .map { Utils.readValue(it, ConversationGraph::class.java) }
        .flatMap { it.toList() }
        .toMap()

    @JvmStatic
    fun getNoteById(noteId: String): ConversationGraph = notes[noteId]!!

}
