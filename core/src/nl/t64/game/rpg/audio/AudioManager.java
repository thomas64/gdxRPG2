package nl.t64.game.rpg.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AudioManager {

    private static final float BGM_VOLUME = 0.2f;
    private static final float BGS_VOLUME = 0.5f;

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
            handle(AudioCommand.BGM_STOP_ALL);
        }
    }

    public void toggleSound(boolean isSoundOn) {
        if (isSoundOn) {
            handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        } else {
            handle(AudioCommand.SE_STOP, AudioEvent.SE_MENU_CONFIRM);
        }
    }

    public void possibleBgmFade(AudioEvent currentBgm, AudioEvent newBgm) {
        if (currentBgm != newBgm) {
            queuedBgm.values().forEach(bgm -> fade(bgm, BGM_VOLUME));
        }
    }

    public void possibleBgsFade(List<AudioEvent> currentBgs, List<AudioEvent> newBgs) {
        currentBgs.stream()
                  .filter(event -> !event.equals(AudioEvent.NONE))
                  .filter(event -> !newBgs.contains(event))
                  .forEach(event -> fade(queuedBgs.get(event.filePath), BGS_VOLUME));
    }

    public void possibleBgmSwitch(AudioEvent prevBgm, AudioEvent nextBgm) {
        if (prevBgm != nextBgm) {
            handle(AudioCommand.BGM_STOP_ALL);
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

    public void fadeBgmBgs() {
        queuedBgm.values().forEach(bgm -> fade(bgm, BGM_VOLUME));
        queuedBgs.values().forEach(bgs -> fade(bgs, BGS_VOLUME));
    }

    public void handle(AudioCommand command, List<AudioEvent> events) {
        events.forEach(event -> handle(command, event));
    }

    public void handle(AudioCommand command, AudioEvent event) {
        switch (command) {
            case BGM_PLAY_ONCE -> playBgm(event.filePath, false);
            case BGM_PLAY_LOOP -> playBgm(event.filePath, true);
            case BGM_STOP -> queuedBgm.get(event.filePath).stop();
            case BGM_PAUSE -> queuedBgm.get(event.filePath).pause();

            case BGS_PLAY_ONCE -> playBgs(event.filePath, false);
            case BGS_PLAY_LOOP -> playBgs(event.filePath, true);
            case BGS_STOP -> queuedBgs.get(event.filePath).stop();
            case BGS_PAUSE -> queuedBgs.get(event.filePath).pause();

            case ME_PLAY_ONCE -> playMe(event.filePath, false);
            case ME_PLAY_LOOP -> playMe(event.filePath, true);
            case ME_STOP -> queuedMe.get(event.filePath).stop();

            case SE_PLAY_ONCE -> playSe(event, false);
            case SE_PLAY_LOOP -> playSe(event, true);
            case SE_STOP -> queuedSe.get(event.filePath).stop();
            default -> throw new IllegalArgumentException("Call 'ALL' AudioCommands without second argument.");
        }
    }

    public void handle(AudioCommand command) {
        switch (command) {
            case BGM_STOP_ALL -> queuedBgm.values().forEach(Music::stop);
            case BGM_PAUSE_ALL -> queuedBgm.values().forEach(Music::pause);

            case BGS_STOP_ALL -> queuedBgs.values().forEach(Music::stop);
            case BGS_PAUSE_ALL -> queuedBgs.values().forEach(Music::pause);

            case SE_STOP_ALL -> queuedSe.values().forEach(Sound::stop);
            default -> throw new IllegalArgumentException("Call non-'ALL' AudioCommands with second argument.");
        }
    }

    public void dispose() {
        queuedBgm.values().forEach(Music::dispose);
        queuedBgs.values().forEach(Music::dispose);
        queuedMe.values().forEach(Sound::dispose);
        queuedSe.values().forEach(Sound::dispose);
    }

    private void playBgm(String filePath, boolean isLooping) {
        if (filePath.isEmpty()) {
            return;
        }
        Music bgm;
        if (queuedBgm.containsKey(filePath)) {
            bgm = queuedBgm.get(filePath);
        } else {
            bgm = Utils.getResourceManager().getMusicAsset(filePath);
            queuedBgm.put(filePath, bgm);
        }
        if (Utils.getSettings().isMusicOn()) {
            bgm.setLooping(isLooping);
            bgm.play();
            bgm.setVolume(BGM_VOLUME);
        } else {
            bgm.stop();
        }
    }

    private void playBgs(String filePath, boolean isLooping) {
        if (filePath.isEmpty()) {
            return;
        }
        Music bgs;
        if (queuedBgs.containsKey(filePath)) {
            bgs = queuedBgs.get(filePath);
        } else {
            bgs = Utils.getResourceManager().getMusicAsset(filePath);
            queuedBgs.put(filePath, bgs);
        }
        if (Utils.getSettings().isSoundOn()) {
            bgs.setLooping(isLooping);
            bgs.play();
            bgs.setVolume(BGS_VOLUME);
        } else {
            bgs.stop();
        }
    }

    private void playMe(String filePath, boolean isLooping) {
        Sound me;
        if (queuedMe.containsKey(filePath)) {
            me = queuedMe.get(filePath);
        } else {
            me = Utils.getResourceManager().getSoundAsset(filePath);
            queuedMe.put(filePath, me);
        }
        if (Utils.getSettings().isMusicOn()) {
            long meId = me.play();
            me.setLooping(meId, isLooping);
        } else {
            me.stop();
        }
    }

    private void playSe(AudioEvent event, boolean isLooping) {
        String filePath = event.filePath;
        float volume = event.volume;
        Sound se;
        if (queuedSe.containsKey(filePath)) {
            se = queuedSe.get(filePath);
        } else {
            se = Utils.getResourceManager().getSoundAsset(filePath);
            queuedSe.put(filePath, se);
        }
        if (Utils.getSettings().isSoundOn()) {
            long seId = se.play();
            se.setVolume(seId, volume);
            se.setLooping(seId, isLooping);
        } else {
            se.stop();
        }
    }

    private void fade(Music bgmBgs, float defaultVolume) {
        if (bgmBgs.isPlaying()) {
            float volume = bgmBgs.getVolume();
            if (volume > 0f) {
                volume -= (defaultVolume / Constant.FADE_DURATION) * Gdx.graphics.getDeltaTime();
                bgmBgs.setVolume(Math.max(volume, 0f));
            } else {
                bgmBgs.setVolume(defaultVolume);
                bgmBgs.pause();
            }
        }
    }

}
