package br.meetingplace.server.modules.user.dao.factory

import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.routers.user.requests.UserCreationData

interface UserFactoryInterface {
    fun create(newUser: UserCreationData, rwUser: UserDBInterface): Boolean
}