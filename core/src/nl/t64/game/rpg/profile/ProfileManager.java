package nl.t64.game.rpg.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import nl.t64.game.rpg.constants.ProfileEvent;

import java.util.*;


public class ProfileManager {

    private static final ProfileManager INSTANCE = new ProfileManager();
    private static final String DEFAULT_PROFILE = "default";
    private static final String SAVEGAME_SUFFIX = ".dat";

    private List<ProfileObserver> observers = new ArrayList<>();

    private Json json = new Json();
    private Map<String, FileHandle> profiles = new HashMap<>();
    private ObjectMap<String, Object> profileProperties = new ObjectMap<>();
    private String profileName = null;

    private ProfileManager() {
        storeAllProfiles();
    }

    public static ProfileManager getInstance() {
        return INSTANCE;
    }

    public void storeAllProfiles() {
        profiles.clear();
        FileHandle[] files = Gdx.files.local("savegame").list(SAVEGAME_SUFFIX);
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
        String fullFilename = profileName + SAVEGAME_SUFFIX;
        FileHandle file = Gdx.files.local("savegame/" + fullFilename);
        file.writeString("testo", false);
        profiles.put(profileName, file);
    }

    public Array<String> getProfileList() {
        Array<String> profilesArray = new Array<>();
        profiles.keySet().forEach(profilesArray::add);
        return profilesArray;
    }

    public Optional<FileHandle> getProfileFile(String profileName) {
        return Optional.ofNullable(profiles.get(profileName));
    }

    public Optional<Object> getProperty(String key) {
        return Optional.ofNullable(profileProperties.get(key));
    }

    public void setProperty(String key, Object object) {
        profileProperties.put(key, object);
    }

    public void saveProfile() {
        notify(ProfileEvent.SAVE_PROFILE);
        String text = json.prettyPrint(json.toJson(profileProperties));
        createNewProfile(profileName, text);
    }

    @SuppressWarnings("unchecked")
    public void loadProfile() {
        String fullFilename = profileName + SAVEGAME_SUFFIX;
        boolean doesProfileExist = Gdx.files.local(fullFilename).exists();
        if (!doesProfileExist) {
            return;
        }
        profileProperties = json.fromJson(ObjectMap.class, profiles.get(profileName));
        notify(ProfileEvent.LOAD_PROFILE);
    }

    public void createNewProfile(String profileName, String fileData) {
        String fullFilename = profileName + SAVEGAME_SUFFIX;
        FileHandle file = Gdx.files.local(fullFilename);
        file.writeString(fileData, false);
        profiles.put(profileName, file);
    }

    public void setCurrentProfile(String newProfileName) {
        if (doesProfileExist(newProfileName)) {
            profileName = newProfileName;
        } else {
            profileName = DEFAULT_PROFILE;
        }
    }

    public void addObserver(ProfileObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ProfileObserver observer) {
        observers.remove(observer);
    }

    private void notify(ProfileEvent event) {
        for (ProfileObserver observer : observers) {
            observer.onNotify(this, event);
        }

    }

}
