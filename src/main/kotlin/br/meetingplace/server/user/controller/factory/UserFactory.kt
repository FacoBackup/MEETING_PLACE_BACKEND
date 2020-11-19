package br.meetingplace.server.user.controller.factory

import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.user.UserCreationData
import br.meetingplace.server.user.classes.User


class UserFactory private constructor() : UserFactoryInterface {

    companion object {
        private val Class = UserFactory()
        fun getClass() = Class
    }

    override fun create(newUser: UserCreationData, rwUser: UserLSInterface): Boolean {
        val user = User(newUser.userName, newUser.age, newUser.email.toLowerCase(), newUser.password)

        return if (newUser.age >= 16) {
            val existingUser = rwUser.load(user.getEmail())
            println (existingUser)
            return if (existingUser == null) {
                rwUser.store(user)
                true
            } else false
        } else false
    }
}