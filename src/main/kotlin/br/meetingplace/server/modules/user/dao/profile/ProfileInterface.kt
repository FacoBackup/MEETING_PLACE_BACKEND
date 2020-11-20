package br.meetingplace.server.modules.user.dao.profile

import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.routers.generic.requests.Login
import br.meetingplace.server.routers.user.requests.ProfileData

interface ProfileInterface {
    fun updateProfile(newProfile: ProfileData, rwUser: UserDBInterface)
    fun clearNotifications(data: Login, rwUser: UserDBInterface)
}