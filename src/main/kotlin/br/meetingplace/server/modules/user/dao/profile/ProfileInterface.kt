package br.meetingplace.server.modules.user.dao.profile

import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.requests.generic.Login
import br.meetingplace.server.requests.users.ProfileData

interface ProfileInterface {
    fun updateProfile(newProfile: ProfileData, rwUser: UserDBInterface)
    fun clearNotifications(data: Login, rwUser: UserDBInterface)
}