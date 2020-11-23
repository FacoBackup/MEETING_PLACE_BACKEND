package br.meetingplace.server.modules.user.dao.factory

import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.user.dto.User
import br.meetingplace.server.requests.users.data.UserCreationData


class UserFactory private constructor() {

    companion object {
        private val Class = UserFactory()
        fun getClass() = Class
    }

    fun create(newUser: UserCreationData, rwUser: UserDBInterface): Status {
        val user = User(newUser.userName, newUser.age, newUser.email.toLowerCase(), newUser.password)
        return if (!rwUser.check(user.getEmail())) {
            rwUser.insert(user)
            Status(statusCode = 200, StatusMessages.OK)
        } else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }
}