package nl.t64.game.rpg.subjects;

import nl.t64.game.rpg.ProfileManager;

import java.util.ArrayList;
import java.util.List;


public class ProfileSubject {

    private final List<ProfileObserver> observers = new ArrayList<>();

    public void addObserver(ProfileObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ProfileObserver observer) {
        observers.remove(observer);
    }

    public void notifyCreateProfile(ProfileManager profileManager) {
        observers.forEach(observer -> observer.onNotifyCreateProfile(profileManager));
    }

    public void notifySaveProfile(ProfileManager profileManager) {
        observers.forEach(observer -> observer.onNotifySaveProfile(profileManager));
    }

    public void notifyLoadProfile(ProfileManager profileManager) {
        observers.forEach(observer -> observer.onNotifyLoadProfile(profileManager));
    }

}
