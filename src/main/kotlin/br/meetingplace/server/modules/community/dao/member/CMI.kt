package br.meetingplace.server.modules.community.dao.member


import br.meetingplace.server.modules.community.dto.response.CommunityMemberDTO
import io.ktor.http.*

interface CMI {
    fun create(userID: String, communityID: String, role: String): HttpStatusCode
    fun read(communityID: String, userID: String): CommunityMemberDTO?
    fun check(communityID: String, userID: String): HttpStatusCode
    fun update(communityID: String, userID: String, role: String): HttpStatusCode
    fun delete(communityID: String, userID: String): HttpStatusCode
}