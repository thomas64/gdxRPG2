package nl.t64.game.rpg.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.*;


public class ProfileManager {

    private static final ProfileManager INSTANCE = new ProfileManager();
    private static final String SAVE_PATH = "savegame/";
    private static final String DEFAULT_PROFILE = "default";
    private static final String SAVEGAME_SUFFIX = ".dat";

    private List<ProfileObserver> observers = new ArrayList<>();

    private Json json = new Json();
    private Map<String, FileHandle> profiles = new HashMap<>();
    private ObjectMap<String, Object> profileProperties = new ObjectMap<>();
    private String profileName = null;

    private ProfileManager() {
    }

    public static ProfileManager getInstance() {
        return INSTANCE;
    }

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
        observers.forEach(ProfileObserver::onNotifyCreate);
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
        observers.forEach(ProfileObserver::onNotifySave);
        writeProfileToDisk();
    }

    @SuppressWarnings("unchecked")
    public void loadProfile(String selectedProfileName) {
        String fullFilename = selectedProfileName + SAVEGAME_SUFFIX;
        boolean doesProfileExist = Gdx.files.local(SAVE_PATH + fullFilename).exists();
        if (!doesProfileExist) {
            return;
        }
        profileName = selectedProfileName;
        profileProperties = json.fromJson(ObjectMap.class, profiles.get(profileName));
        observers.forEach(ProfileObserver::onNotifyLoad);
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
