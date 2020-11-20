package br.meetingplace.server.modules.user.dao.factory

import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.requests.users.data.UserCreationData

interface UserFactoryInterface {
    fun create(newUser: UserCreationData, rwUser: UserDBInterface): Boolean
}