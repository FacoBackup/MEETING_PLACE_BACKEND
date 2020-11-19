package br.meetingplace.server.user.controller.factory

import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.user.UserCreationData

interface UserFactoryInterface {
    fun create(newUser: UserCreationData, rwUser: UserLSInterface): Boolean
}