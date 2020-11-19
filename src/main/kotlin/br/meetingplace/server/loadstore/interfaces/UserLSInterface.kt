package br.meetingplace.server.loadstore.interfaces

import br.meetingplace.server.user.classes.User

interface UserLSInterface {
    fun load(id: String): User?
    fun store(data: User)
    fun delete(data: User)
}