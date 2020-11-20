package br.meetingplace.server.modules.user.dao.profile

import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.requests.generic.Login
import br.meetingplace.server.requests.users.ProfileData

class Profile private constructor() : ProfileInterface {

    companion object {
        private val Class = Profile()
        fun getClass() = Class
    }

    override fun updateProfile(newProfile: ProfileData, rwUser: UserDBInterface) {
        val user = rwUser.select(newProfile.login.email)

        if (user != null) {
            user.updateProfile(
                    if (!newProfile.about.isNullOrBlank()) newProfile.about else user.getAbout(),
                    if (!newProfile.nationality.isNullOrBlank()) newProfile.nationality else user.getNationality(),
                    if (!newProfile.gender.isNullOrBlank()) newProfile.gender else user.getGender()
            )
            rwUser.insert(user)
        }
    }

    override fun clearNotifications(data: Login, rwUser: UserDBInterface) {
        val user = rwUser.select(data.email)
        if(user != null){
            user.clearNotifications()
            rwUser.insert(user)
        }
    }
}