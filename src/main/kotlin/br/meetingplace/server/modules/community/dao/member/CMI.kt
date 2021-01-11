package br.meetingplace.server.modules.community.dao.member


import br.meetingplace.server.modules.community.dto.response.CommunityMemberDTO
import io.ktor.http.*

interface CMI {
    suspend fun create(userID: String, communityID: String, role: String): HttpStatusCode
    suspend fun read(communityID: String, userID: String): CommunityMemberDTO?
    suspend fun readByCommunity(communityID: String): List<CommunityMemberDTO>
    suspend fun readByUser(userID: String): List<CommunityMemberDTO>
    suspend fun readMods(communityID: String): List<CommunityMemberDTO>
    suspend fun readFollowers(communityID: String): List<CommunityMemberDTO>
    suspend fun readMembers(communityID: String): List<CommunityMemberDTO>
    suspend fun check(communityID: String, userID: String): HttpStatusCode
    suspend fun update(communityID: String, userID: String, role: String): HttpStatusCode
    suspend fun delete(communityID: String, userID: String): HttpStatusCode
}