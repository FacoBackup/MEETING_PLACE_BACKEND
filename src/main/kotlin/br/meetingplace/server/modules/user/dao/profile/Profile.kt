package br.meetingplace.server.modules.user.dao.profile

import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.requests.generic.data.Login
import br.meetingplace.server.requests.users.data.ProfileData

class Profile private constructor() {

    companion object {
        private val Class = Profile()
        fun getClass() = Class
    }

    fun updateProfile(data: ProfileData, userDB: UserDBInterface) : Status {
        val user = userDB.select(data.login.email)

        return if (user != null) {
            if(data.about != null)
                user.setAbout(data.about)
            if(data.gender != null)
                user.setGender(data.gender)
            if(data.nationality != null)
                user.setNationality(data.nationality)
            if(data.imageURL != null)
                user.setImageURL(data.imageURL)
            return userDB.insert(user)
        } else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }
    fun clearNotifications(data: Login, userDB: UserDBInterface):Status {
        val user = userDB.select(data.email)
        return if (user != null) {
            user.setInbox(listOf())
            return userDB.insert(user)
        } else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }
}