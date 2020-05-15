package nl.t64.game.rpg.profile;

import java.util.ArrayList;
import java.util.List;


class ProfileSubject {

    private final List<ProfileObserver> observers = new ArrayList<>();

    public void addObserver(ProfileObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ProfileObserver observer) {
        observers.remove(observer);
    }

    void notifyCreateProfile(ProfileManager profileManager) {
        observers.forEach(observer -> observer.onNotifyCreateProfile(profileManager));
    }

    void notifySaveProfile(ProfileManager profileManager) {
        observers.forEach(observer -> observer.onNotifySaveProfile(profileManager));
    }

    void notifyLoadProfile(ProfileManager profileManager) {
        observers.forEach(observer -> observer.onNotifyLoadProfile(profileManager));
    }

}
