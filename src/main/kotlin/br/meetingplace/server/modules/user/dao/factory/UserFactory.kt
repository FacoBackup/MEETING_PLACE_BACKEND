package br.meetingplace.server.modules.user.dao.factory

import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.user.dto.User
import br.meetingplace.server.requests.users.UserCreationData


class UserFactory private constructor() : UserFactoryInterface {

    companion object {
        private val Class = UserFactory()
        fun getClass() = Class
    }

    override fun create(newUser: UserCreationData, rwUser: UserDBInterface): Boolean {
        val user = User(newUser.userName, newUser.age, newUser.email.toLowerCase(), newUser.password)

        return if (newUser.age >= 16) {
            val existingUser = rwUser.select(user.getEmail())
            println (existingUser)
            return if (existingUser == null) {
                rwUser.insert(user)
                true
            } else false
        } else false
    }
}