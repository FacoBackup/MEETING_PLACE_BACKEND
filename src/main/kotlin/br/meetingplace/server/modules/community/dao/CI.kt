package br.meetingplace.server.modules.community.dao

import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.community.dto.response.CommunityDTO
import br.meetingplace.server.modules.community.dto.response.SimplifiedCommunityDTO
import br.meetingplace.server.modules.community.dto.response.SimplifiedUserCommunityDTO
import io.ktor.http.*

interface CI {
    suspend fun create(data: RequestCommunityCreation):HttpStatusCode
    suspend fun read(id: Long): CommunityDTO?
    suspend fun update(communityID: Long,
                       imageURL: String?,
                       backgroundImageURL: String?,
                       about: String?):HttpStatusCode
    suspend fun readRelatedCommunities(id: Long): List<CommunityDTO>
    suspend fun check(id: Long):Boolean
    suspend fun searchByNewest(name: String): List<SimplifiedCommunityDTO>
    suspend fun searchByMaxID(name: String, maxID: Long): List<SimplifiedCommunityDTO>
    suspend fun readByExactName (name: String):  CommunityDTO?
    suspend fun delete(id: Long):HttpStatusCode
}