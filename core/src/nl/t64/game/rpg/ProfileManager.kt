package nl.t64.game.rpg

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.SerializationException
import ktx.collections.GdxArray
import ktx.collections.GdxMap
import ktx.collections.set
import ktx.json.fromJson
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.constants.Constant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private const val SAVE_FILES = "save1.dat,save2.dat,save3.dat,save4.dat,save5.dat,save6.dat,save7.dat"
private const val LOADING = ",,Loading...,,,,"
private const val SAVE_STATE_KEY = "saveState"
private const val PROFILE_ID = "id"
private const val PROFILE_SAVE_DATE = "saveDate"
private const val DATE_PATTERN = "yyyy-MM-dd HH:mm"
private const val INVALID_PROFILE_VIEW = " [Invalid]"
private const val DEFAULT_EMPTY_PROFILE_VIEW = " [...]"

class ProfileManager {

    private val json = Json()
    private var saveStateProperties = GdxMap<String, Any>()
    private lateinit var currentSaveFileName: String
    var selectedIndex = -1

    fun doesProfileExist(profileIndex: Int): Boolean {
        val saveFile = getSaveFileBy(profileIndex)
        return saveFile.contains(SAVE_STATE_KEY)
    }

    fun createNewProfile(profileId: String) {
        currentSaveFileName = getSaveFileNames()[selectedIndex]
        saveStateProperties.clear()
        setProperty(PROFILE_ID, profileId.ifEmpty { getDefaultId() })
        brokerManager.profileObservers.notifyCreateProfile(this)
        writeProfileToDisk()
    }

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T> getProperty(key: String): T {
        return saveStateProperties[key] as T
    }

    fun setProperty(key: String, any: Any) {
        saveStateProperties[key] = any
    }

    fun saveProfile() {
        brokerManager.profileObservers.notifySaveProfile(this)
        writeProfileToDisk()
    }

    fun loadProfile(profileIndex: Int) {
        currentSaveFileName = getSaveFileNames()[profileIndex]
        val saveFile = Gdx.app.getPreferences(currentSaveFileName)
        saveStateProperties = getSaveStateProperties(saveFile)
        brokerManager.profileObservers.notifyLoadProfile(this)
    }

    fun removeProfile(profileIndex: Int) {
        val saveFile = getSaveFileBy(profileIndex)
        saveFile.clear()
        saveFile.flush()
    }

    fun getVisualLoadingArray(): GdxArray<String> =
        GdxArray(LOADING.split(",").toTypedArray())

    fun getVisualProfileArray(): GdxArray<String> =
        GdxArray(getSaveFileNames().indices.map { getVisualOf(it) }.toTypedArray())

    private fun writeProfileToDisk() {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        setProperty(PROFILE_SAVE_DATE, formatter.format(LocalDateTime.now()))
        val saveStateJsonString = json.prettyPrint(json.toJson(saveStateProperties))
        val saveFile = Gdx.app.getPreferences(currentSaveFileName)
        saveFile.putString(SAVE_STATE_KEY, saveStateJsonString)
        saveFile.flush()
    }

    private fun getVisualOf(profileIndex: Int): String {
        val saveFile = getSaveFileBy(profileIndex)
        return if (saveFile.contains(SAVE_STATE_KEY)) {
            try {
                val saveState = getSaveStateProperties(saveFile)
                "${saveState[PROFILE_ID]} [${saveState[PROFILE_SAVE_DATE]}]"
            } catch (e: SerializationException) {
                "${profileIndex + 1} $INVALID_PROFILE_VIEW"
            }
        } else {
            "${profileIndex + 1} $DEFAULT_EMPTY_PROFILE_VIEW"
        }
    }

    private fun getSaveFileBy(profileIndex: Int): Preferences {
        val saveFileName = getSaveFileNames()[profileIndex]
        return Gdx.app.getPreferences(saveFileName)
    }

    private fun getSaveStateProperties(saveFile: Preferences): GdxMap<String, Any> {
        val saveStateJsonString = saveFile.getString(SAVE_STATE_KEY)
        return json.fromJson(saveStateJsonString)
    }

    private fun getDefaultId(): String {
        return "${Constant.PLAYER_ID.substring(0, 1).uppercase()} ${Constant.PLAYER_ID.substring(1)}"
    }

    private fun getSaveFileNames(): List<String> = SAVE_FILES.split(",")

}
