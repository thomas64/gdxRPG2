package nl.t64.game.rpg.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import nl.t64.game.rpg.constants.Constant;

import java.util.*;


public class ProfileManager {

    private static final String SAVE_PATH = "savegame/";
    private static final String DEFAULT_PROFILE = Constant.PLAYER_ID;
    private static final String SAVEGAME_SUFFIX = ".dat";

    private final List<ProfileObserver> observers = new ArrayList<>();

    private final Json json = new Json();
    private final Map<String, FileHandle> profiles = new HashMap<>();
    private ObjectMap<String, Object> profileProperties = new ObjectMap<>();
    private String profileName = null;

    public void loadAllProfiles() {
        profiles.clear();
        FileHandle[] files = Gdx.files.local(SAVE_PATH).list(SAVEGAME_SUFFIX);
        Arrays.stream(files).forEach(file -> profiles.put(file.nameWithoutExtension(), file));
    }

    public boolean doesProfileExist(String newProfileName) {
        if (newProfileName.isEmpty()) {
            newProfileName = DEFAULT_PROFILE;
        }
        return profiles.containsKey(newProfileName);
    }

    public void createNewProfile(String newProfileName) {
        if (newProfileName.isEmpty()) {
            newProfileName = DEFAULT_PROFILE;
        }
        profileName = newProfileName;
        observers.forEach(observer -> observer.onNotifyCreate(this));
        writeProfileToDisk();
    }

    public Array<String> getProfileList() {
        Array<String> profilesArray = new Array<>();
        profiles.keySet().forEach(profilesArray::add);
        return profilesArray;
    }

    public <T> T getProperty(String key, Class<T> clazz) {
        return clazz.cast(profileProperties.get(key));
    }

    public void setProperty(String key, Object object) {
        profileProperties.put(key, object);
    }

    public void saveProfile() {
        observers.forEach(observer -> observer.onNotifySave(this));
        writeProfileToDisk();
    }

    @SuppressWarnings("unchecked")
    public void loadProfile(String selectedProfileName) {
        String fullFilename = selectedProfileName + SAVEGAME_SUFFIX;
        boolean doesProfileExist = Gdx.files.local(SAVE_PATH + fullFilename).exists();
        if (!doesProfileExist) {
            throw new GdxRuntimeException("Profile does not exist.");
        }
        profileName = selectedProfileName;
        profileProperties = json.fromJson(ObjectMap.class, profiles.get(profileName));
        observers.forEach(observer -> observer.onNotifyLoad(this));
    }

    public Array<String> removeProfile(String selectedProfileName) {
        String fullFilename = selectedProfileName + SAVEGAME_SUFFIX;
        boolean doesProfileExist = Gdx.files.local(SAVE_PATH + fullFilename).exists();
        if (doesProfileExist) {
            Gdx.files.local(SAVE_PATH + fullFilename).delete();
            loadAllProfiles();
        }
        return getProfileList();
    }

    public void addObserver(ProfileObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ProfileObserver observer) {
        observers.remove(observer);
    }

    private void writeProfileToDisk() {
        String fileData = json.prettyPrint(json.toJson(profileProperties));
        String fullFilename = profileName + SAVEGAME_SUFFIX;
        FileHandle file = Gdx.files.local(SAVE_PATH + fullFilename);
        file.writeString(fileData, false);
        profiles.put(profileName, file);
    }

}
