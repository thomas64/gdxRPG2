package nl.t64.game.rpg.subjects;

import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.screens.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;


public class ComponentSubject {

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

    public void notifyShowConversationDialog(String conversationId, Entity npcCharacter) {
        observers.forEach(observer -> observer.onNotifyShowConversationDialog(conversationId, npcCharacter));
    }

    public void notifyShowConversationDialog(String conversationId, String characterId) {
        observers.forEach(observer -> observer.onNotifyShowConversationDialog(conversationId, characterId));
    }

    public void notifyShowNoteDialog(String noteId) {
        observers.forEach(observer -> observer.onNotifyShowNoteDialog(noteId));
    }

    public void notifyShowFindDialog(Loot loot, AudioEvent event, String message) {
        observers.forEach(observer -> observer.onNotifyShowFindDialog(loot, event, message));
    }

    public void notifyShowFindDialog(Loot loot, AudioEvent event) {
        observers.forEach(observer -> observer.onNotifyShowFindDialog(loot, event));
    }

    public void notifyShowMessageDialog(String message) {
        observers.forEach(observer -> observer.onNotifyShowMessageDialog(message));
    }

}
