package nl.t64.game.rpg.components.character;

import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;

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

    void notifyShowConversationDialog(String conversationId, Character npcCharacter) {
        observers.forEach(
                observer -> observer.onNotifyShowConversationDialog(conversationId, npcCharacter));
    }

    void notifyShowNoteDialog(String noteId) {
        observers.forEach(observer -> observer.onNotifyShowNoteDialog(noteId));
    }

    void notifyShowFindDialog(Loot loot, AudioEvent event, String message) {
        observers.forEach(observer -> observer.onNotifyShowFindDialog(loot, event, message));
    }

    void notifyShowFindDialog(Loot loot, AudioEvent event) {
        observers.forEach(observer -> observer.onNotifyShowFindDialog(loot, event));
    }

    void notifyShowMessageDialog(String message) {
        observers.forEach(observer -> observer.onNotifyShowMessageDialog(message));
    }

}
