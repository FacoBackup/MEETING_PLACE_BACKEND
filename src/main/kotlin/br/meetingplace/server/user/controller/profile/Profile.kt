package br.meetingplace.server.user.controller.profile

import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.Login
import br.meetingplace.server.dto.user.ProfileData

class Profile private constructor() : ProfileInterface {

    companion object {
        private val Class = Profile()
        fun getClass() = Class
    }

    override fun updateProfile(newProfile: ProfileData, rwUser: UserLSInterface) {
        val user = rwUser.load(newProfile.login.email)

        if (user != null) {
            user.updateProfile(
                    if (!newProfile.about.isNullOrBlank()) newProfile.about else user.getAbout(),
                    if (!newProfile.nationality.isNullOrBlank()) newProfile.nationality else user.getNationality(),
                    if (!newProfile.gender.isNullOrBlank()) newProfile.gender else user.getGender()
            )
            rwUser.store(user)
        }
    }

    override fun clearNotifications(data: Login, rwUser: UserLSInterface) {
        val user = rwUser.load(data.email)
        if(user != null){
            user.clearNotifications()
            rwUser.store(user)
        }
    }
}