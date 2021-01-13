package br.meetingplace.server.modules.community.dao

import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.community.dto.response.CommunityDTO
import io.ktor.http.*

interface CI {
    suspend fun create(data: RequestCommunityCreation):HttpStatusCode
    suspend fun read(id: String): CommunityDTO?
    suspend fun update(communityID: String,
                       imageURL: String?,
                       backgroundImageURL: String?,
                       about: String?):HttpStatusCode
    suspend fun readParentCommunities(communityID: String): List<CommunityDTO>
    suspend fun check(id: String):Boolean
    suspend fun readByName(name: String): List<CommunityDTO>
    suspend fun readByExactName (name: String):  CommunityDTO?
    suspend fun delete(id: String):HttpStatusCode
}