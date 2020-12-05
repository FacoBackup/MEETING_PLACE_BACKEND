package br.meetingplace.server.modules.group.dao

import br.meetingplace.server.modules.group.dto.response.GroupDTO
import br.meetingplace.server.modules.group.dto.requests.RequestGroupCreation
import io.ktor.http.*

interface GI {
    fun create(data: RequestGroupCreation, approved: Boolean): HttpStatusCode
    fun read(groupID: String): GroupDTO?
    fun check(groupID: String): HttpStatusCode
    fun update(groupID: String, name: String?, about: String?, approved: Boolean): HttpStatusCode
    fun delete(groupID: String): HttpStatusCode
}