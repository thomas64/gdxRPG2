package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import lombok.Getter;
import nl.t64.game.rpg.constants.Constant;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Settings {

    private static final String SETTINGS_FILE = "settings/settings.dat";

    @Getter
    private boolean fullscreen;

    private Settings() {
        this.fullscreen = false;
    }

    public static Settings getSettingsFromFile() throws IOException {
        Path path = Paths.get(SETTINGS_FILE);
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            Files.writeString(path, "{}");
        } catch (FileAlreadyExistsException e) {
            Stream<String> lines = Files.lines(path);
            String settingsFile = lines.collect(Collectors.joining(System.lineSeparator()));
            lines.close();
            Json json = new Json();
            return json.fromJson(Settings.class, settingsFile);
        }
        return new Settings();
    }

    public void toggleFullscreen() {
        if (Gdx.graphics.isFullscreen()) {
            setWindowedMode();
        } else {
            setFullscreenMode();
        }
        writeSettingsFile();
    }

    private void setWindowedMode() {
        Gdx.graphics.setWindowedMode(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
        Gdx.input.setCursorCatched(false);
    }

    private void setFullscreenMode() {
        Graphics.DisplayMode displayMode = Arrays.stream(Gdx.graphics.getDisplayModes())
                                                 .filter(mode -> mode.width == Constant.SCREEN_WIDTH
                                                         && mode.height == Constant.SCREEN_HEIGHT)
                                                 .max(Comparator.comparing(mode -> mode.refreshRate))
                                                 .orElseThrow(NoSuchElementException::new);
        Gdx.graphics.setFullscreenMode(displayMode);
        Gdx.input.setCursorCatched(true);
    }

    private void writeSettingsFile() {
        fullscreen = Gdx.graphics.isFullscreen();
        Json json = new Json();
        String fileData = json.prettyPrint(json.toJson(this));
        FileHandle file = Gdx.files.local(SETTINGS_FILE);
        file.writeString(fileData, false);
    }

}
