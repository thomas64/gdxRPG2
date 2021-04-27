package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import lombok.Setter;
import nl.t64.game.rpg.constants.Constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;


public class ProfileManager {

    private static final String DEFAULT_ID =
            Constant.PLAYER_ID.substring(0, 1).toUpperCase() + Constant.PLAYER_ID.substring(1);
    private static final String SAVE1 = "save1.dat";
    private static final String SAVE2 = "save2.dat";
    private static final String SAVE3 = "save3.dat";
    private static final String SAVE4 = "save4.dat";
    private static final String SAVE5 = "save5.dat";
    private static final List<String> SAVE_FILES = List.of(SAVE1, SAVE2, SAVE3, SAVE4, SAVE5);
    private static final String SAVE_STATE_KEY = "saveState";
    private static final String PROFILE_ID = "id";
    private static final String PROFILE_SAVE_DATE = "saveDate";
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    private final Json json = new Json();
    private ObjectMap<String, Object> saveStateProperties = new ObjectMap<>();
    private String currentSaveFileName = null;
    @Setter
    private int selectedIndex = -1;

    public boolean doesProfileExist(int profileIndex) {
        Preferences saveFile = getSaveFileBy(profileIndex);
        return saveFile.contains(SAVE_STATE_KEY);
    }

    public void createNewProfile(String profileId) {
        if (profileId.isEmpty()) {
            profileId = DEFAULT_ID;
        }
        currentSaveFileName = SAVE_FILES.get(selectedIndex);
        saveStateProperties.clear();
        setProperty(PROFILE_ID, profileId);
        Utils.getBrokerManager().profileObservers.notifyCreateProfile(this);
        writeProfileToDisk();
    }

    public <T> T getProperty(String key, Class<T> clazz) {
        return clazz.cast(saveStateProperties.get(key));
    }

    public void setProperty(String key, Object object) {
        saveStateProperties.put(key, object);
    }

    public void saveProfile() {
        Utils.getBrokerManager().profileObservers.notifySaveProfile(this);
        writeProfileToDisk();
    }

    public void loadProfile(int profileIndex) {
        currentSaveFileName = SAVE_FILES.get(profileIndex);
        Preferences saveFile = Gdx.app.getPreferences(currentSaveFileName);
        saveStateProperties = getSaveStateProperties(saveFile);
        Utils.getBrokerManager().profileObservers.notifyLoadProfile(this);
    }

    public void removeProfile(int profileIndex) {
        Preferences saveFile = getSaveFileBy(profileIndex);
        saveFile.clear();
        saveFile.flush();
    }

    public Array<String> getVisualProfileArray() {
        String[] visualListOfProfiles = IntStream.range(0, SAVE_FILES.size())
                                                 .mapToObj(this::getVisualOf)
                                                 .toArray(String[]::new);
        return new Array<>(visualListOfProfiles);
    }

    private void writeProfileToDisk() {
        var formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        setProperty(PROFILE_SAVE_DATE, formatter.format(LocalDateTime.now()));
        String saveStateJsonString = json.prettyPrint(json.toJson(saveStateProperties));
        Preferences saveFile = Gdx.app.getPreferences(currentSaveFileName);
        saveFile.putString(SAVE_STATE_KEY, saveStateJsonString);
        saveFile.flush();
    }

    private String getVisualOf(int profileIndex) {
        Preferences saveFile = getSaveFileBy(profileIndex);
        if (saveFile.contains(SAVE_STATE_KEY)) {
            ObjectMap<String, Object> objectMap = getSaveStateProperties(saveFile);
            return objectMap.get(PROFILE_ID) + " [" + objectMap.get(PROFILE_SAVE_DATE) + "]";
        } else {
            return (profileIndex + 1) + " [...]";
        }
    }

    private Preferences getSaveFileBy(int profileIndex) {
        String saveFileName = SAVE_FILES.get(profileIndex);
        return Gdx.app.getPreferences(saveFileName);
    }

    private ObjectMap<String, Object> getSaveStateProperties(Preferences saveFile) {
        String saveStateJsonString = saveFile.getString(SAVE_STATE_KEY);
        return json.fromJson(ObjectMap.class, saveStateJsonString);
    }

}
