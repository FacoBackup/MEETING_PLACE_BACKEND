package br.meetingplace.server.db.user

import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.user.dto.User

interface UserDBInterface {
    fun select(id: String): User?
    fun insert(data: User): Status
    fun delete(data: User): Status
    fun check(id: String): Boolean
}