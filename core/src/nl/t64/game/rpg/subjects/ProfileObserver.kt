package nl.t64.game.rpg.subjects

import nl.t64.game.rpg.ProfileManager


interface ProfileObserver {

    fun onNotifyCreateProfile(profileManager: ProfileManager)
    fun onNotifySaveProfile(profileManager: ProfileManager)
    fun onNotifyLoadProfile(profileManager: ProfileManager)

}
