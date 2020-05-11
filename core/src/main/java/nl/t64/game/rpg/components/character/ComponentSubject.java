package nl.t64.game.rpg.components.character;

import java.util.ArrayList;
import java.util.List;


class ComponentSubject {

    private final List<ComponentObserver> observers = new ArrayList<>();

    public void addObserver(ComponentObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ComponentObserver observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    void notifyShowConversationDialog(String conversationId, String characterId, Character npcCharacter) {
        observers.forEach(
                observer -> observer.onNotifyShowConversationDialog(conversationId, characterId, npcCharacter));
    }

    void notifyShowNoteDialog(String noteId) {
        observers.forEach(observer -> observer.onNotifyShowNoteDialog(noteId));
    }

}
