package br.meetingplace.server.modules.user.dao.profile

import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.requests.generic.data.Login
import br.meetingplace.server.requests.users.data.ProfileData

class Profile private constructor() {

    companion object {
        private val Class = Profile()
        fun getClass() = Class
    }

    fun updateProfile(data: ProfileData, userDB: UserDBInterface) {
        val user = userDB.select(data.login.email)

        if (user != null) {
            user.setAbout(data.about)
            user.setGender(data.gender)
            user.setNationality(data.nationality)
            user.setImageURL(data.imageURL)
            userDB.insert(user)
        }
    }
    fun clearNotifications(data: Login, userDB: UserDBInterface) {
        val user = userDB.select(data.email)
        if (user != null) {
            user.setInbox(listOf())
            userDB.insert(user)
        }
    }
}