package br.meetingplace.server.modules.community.dao

import br.meetingplace.server.modules.community.dto.response.CommunityDTO
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import io.ktor.http.*

interface CI {
    fun create(data: RequestCommunityCreation):HttpStatusCode
    fun read(id: String): CommunityDTO?
    fun update(communityID: String,
               name: String?,
               about: String?,
               parentID: String?):HttpStatusCode
    fun check(id: String):HttpStatusCode
    fun delete(id: String):HttpStatusCode
}