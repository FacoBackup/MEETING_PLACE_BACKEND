package br.meetingplace.server.db.interfaces

import br.meetingplace.server.modules.user.dto.User

interface UserDBInterface {
    fun select(id: String): User?
    fun insert(data: User)
    fun delete(data: User)
}