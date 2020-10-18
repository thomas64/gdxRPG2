package nl.t64.game.rpg.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import nl.t64.game.rpg.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AudioManager {

    private static final float BGM_VOLUME = 0.15f;

    private final Map<String, Music> queuedBgm;
    private final Map<String, Music> queuedBgs;
    private final Map<String, Sound> queuedMe;
    private final Map<String, Sound> queuedSe;

    public AudioManager() {
        this.queuedBgm = new HashMap<>();
        this.queuedBgs = new HashMap<>();
        this.queuedMe = new HashMap<>();
        this.queuedSe = new HashMap<>();
    }

    public void toggleMusic(boolean isMusicOn, boolean mustPlayBgmImmediately) {
        if (isMusicOn && mustPlayBgmImmediately) {
            handle(AudioCommand.BGM_PLAY_LOOP, AudioEvent.BGM_TITLE);
        } else {
            handle(AudioCommand.BGM_STOP_ALL, AudioEvent.NONE);
        }
    }

    public void toggleSound(boolean isSoundOn) {
        if (isSoundOn) {
            handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        } else {
            handle(AudioCommand.SE_STOP, AudioEvent.SE_MENU_CONFIRM);
        }
    }

    public void possibleBgmSwitch(AudioEvent prevBgm, AudioEvent nextBgm) {
        if (prevBgm != nextBgm) {
            handle(AudioCommand.BGM_STOP_ALL, AudioEvent.NONE);
            handle(AudioCommand.BGM_PLAY_LOOP, nextBgm);
        }
    }

    public void possibleBgsSwitch(List<AudioEvent> prevBgs, List<AudioEvent> nextBgs) {
        prevBgs.stream()
               .filter(event -> !event.equals(AudioEvent.NONE))
               .filter(event -> !nextBgs.contains(event))
               .forEach(event -> handle(AudioCommand.BGS_STOP, event));
        nextBgs.stream()
               .filter(event -> !event.equals(AudioEvent.NONE))
               .filter(event -> !prevBgs.contains(event))
               .forEach(event -> handle(AudioCommand.BGS_PLAY_LOOP, event));
    }

    public void handle(AudioCommand command, List<AudioEvent> events) {
        events.forEach(event -> handle(command, event));
    }

    public void handle(AudioCommand command, AudioEvent event) {
        switch (command) {
            case BGM_PLAY_ONCE -> playBgm(event.filePath, false);
            case BGM_PLAY_LOOP -> playBgm(event.filePath, true);
            case BGM_STOP -> queuedBgm.get(event.filePath).stop();
            case BGM_STOP_ALL -> queuedBgm.values().forEach(Music::stop);
            case BGM_PAUSE -> queuedBgm.get(event.filePath).pause();
            case BGM_PAUSE_ALL -> queuedBgm.values().forEach(Music::pause);

            case BGS_PLAY_ONCE -> playBgs(event.filePath, false);
            case BGS_PLAY_LOOP -> playBgs(event.filePath, true);
            case BGS_STOP -> queuedBgs.get(event.filePath).stop();
            case BGS_STOP_ALL -> queuedBgs.values().forEach(Music::stop);
            case BGS_PAUSE -> queuedBgs.get(event.filePath).pause();
            case BGS_PAUSE_ALL -> queuedBgs.values().forEach(Music::pause);

            case ME_PLAY_ONCE -> playMe(event.filePath, false);
            case ME_PLAY_LOOP -> playMe(event.filePath, true);
            case ME_STOP -> queuedMe.get(event.filePath).stop();

            case SE_PLAY_ONCE -> playSe(event, false);
            case SE_PLAY_LOOP -> playSe(event, true);
            case SE_STOP -> queuedSe.get(event.filePath).stop();
        }
    }

    public void dispose() {
        queuedBgm.values().forEach(Music::dispose);
        queuedBgs.values().forEach(Music::dispose);
        queuedMe.values().forEach(Sound::dispose);
        queuedSe.values().forEach(Sound::dispose);
    }

    private void playBgm(String filePath, boolean isLooping) {
        if (Utils.getSettings().isMusicOn()) {
            if (queuedBgm.containsKey(filePath)) {
                Music bgm = queuedBgm.get(filePath);
                bgm.setLooping(isLooping);
                bgm.play();
                bgm.setVolume(BGM_VOLUME);
            } else {
                Music bgm = Utils.getResourceManager().getMusicAsset(filePath);
                bgm.setLooping(isLooping);
                bgm.play();
                bgm.setVolume(BGM_VOLUME);
                queuedBgm.put(filePath, bgm);
            }
        }
    }

    private void playBgs(String filePath, boolean isLooping) {
        if (filePath.isEmpty()) {
            return;
        }
        if (Utils.getSettings().isSoundOn()) {
            if (queuedBgs.containsKey(filePath)) {
                Music bgs = queuedBgs.get(filePath);
                bgs.setLooping(isLooping);
                bgs.play();
            } else {
                Music bgs = Utils.getResourceManager().getMusicAsset(filePath);
                bgs.setLooping(isLooping);
                bgs.play();
                queuedBgs.put(filePath, bgs);
            }
        }
    }

    private void playMe(String filePath, boolean isLooping) {
        if (Utils.getSettings().isMusicOn()) {
            if (queuedMe.containsKey(filePath)) {
                Sound me = queuedMe.get(filePath);
                long meId = me.play();
                me.setLooping(meId, isLooping);
            } else {
                Sound me = Utils.getResourceManager().getSoundAsset(filePath);
                long meId = me.play();
                me.setLooping(meId, isLooping);
                queuedMe.put(filePath, me);
            }
        }
    }

    private void playSe(AudioEvent event, boolean isLooping) {
        if (Utils.getSettings().isSoundOn()) {
            String filePath = event.filePath;
            float volume = event.volume;
            if (queuedSe.containsKey(filePath)) {
                Sound se = queuedSe.get(filePath);
                long seId = se.play();
                se.setVolume(seId, volume);
                se.setLooping(seId, isLooping);
            } else {
                Sound se = Utils.getResourceManager().getSoundAsset(filePath);
                long seId = se.play();
                se.setVolume(seId, volume);
                se.setLooping(seId, isLooping);
                queuedSe.put(filePath, se);
            }
        }
    }

}
