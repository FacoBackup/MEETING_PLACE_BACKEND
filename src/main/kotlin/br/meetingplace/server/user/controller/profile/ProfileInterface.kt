package br.meetingplace.server.user.controller.profile

import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.Login
import br.meetingplace.server.dto.user.ProfileData

interface ProfileInterface {
    fun updateProfile(newProfile: ProfileData, rwUser: UserLSInterface)
    fun clearNotifications(data: Login, rwUser: UserLSInterface)
}