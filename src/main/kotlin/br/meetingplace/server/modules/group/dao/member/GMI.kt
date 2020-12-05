package br.meetingplace.server.modules.group.dao.member

import br.meetingplace.server.modules.group.dto.response.GroupMemberDTO
import io.ktor.http.*

interface GMI {
    fun create(userID: String, groupID: String, admin: Boolean): HttpStatusCode
    fun read(userID: String, groupID: String): GroupMemberDTO?
    fun check(groupID: String, userID: String): HttpStatusCode
    fun update(userID: String, groupID: String, admin: Boolean): HttpStatusCode
    fun delete(userID: String, groupID: String): HttpStatusCode
}