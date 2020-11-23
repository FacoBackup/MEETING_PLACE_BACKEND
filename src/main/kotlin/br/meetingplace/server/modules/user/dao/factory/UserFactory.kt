package br.meetingplace.server.modules.user.dao.factory

import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.user.dto.User
import br.meetingplace.server.requests.users.data.UserCreationData


class UserFactory private constructor() {

    companion object {
        private val Class = UserFactory()
        fun getClass() = Class
    }

    fun create(newUser: UserCreationData, rwUser: UserDBInterface): Boolean {
        val user = User(newUser.userName, newUser.age, newUser.email.toLowerCase(), newUser.password)
        return if (!rwUser.check(user.getEmail())) {
            rwUser.insert(user)
            true
        } else false
    }
}