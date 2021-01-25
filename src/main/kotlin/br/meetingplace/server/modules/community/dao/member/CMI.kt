package br.meetingplace.server.modules.community.dao.member


import br.meetingplace.server.modules.community.dto.response.CommunityMemberDTO
import io.ktor.http.*

interface CMI {
    suspend fun readModeratorIn(userID: Long): List<CommunityMemberDTO>
    suspend fun create(userID: Long, communityID: Long, role: String): HttpStatusCode
    suspend fun read(communityID: Long, userID: Long): CommunityMemberDTO?
    suspend fun readByCommunity(communityID: Long): List<CommunityMemberDTO>
    suspend fun readByUser(userID: Long): List<CommunityMemberDTO>
    suspend fun readMods(communityID: Long): List<CommunityMemberDTO>
    suspend fun readModsQuantity(communityID: Long):Long
    suspend fun readFollowers(communityID: Long): List<CommunityMemberDTO>
    suspend fun readFollowersQuantity(communityID: Long):Long
    suspend fun readMembers(communityID: Long): List<CommunityMemberDTO>
    suspend fun readMembersQuantity(communityID: Long):Long
    suspend fun check(communityID: Long, userID: Long): HttpStatusCode
    suspend fun update(communityID: Long, userID: Long, role: String): HttpStatusCode
    suspend fun delete(communityID: Long, userID: Long): HttpStatusCode
}