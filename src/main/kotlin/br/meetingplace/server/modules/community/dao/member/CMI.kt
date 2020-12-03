package br.meetingplace.server.modules.community.dao.member


import br.meetingplace.server.modules.community.dto.response.CommunityMemberDTO
import br.meetingplace.server.response.status.Status

interface CMI {
    fun create(userID: String, communityID: String, role: String): Status
    fun read(communityID: String, userID: String): CommunityMemberDTO?
    fun update(communityID: String, userID: String, role: String): Status
    fun delete(communityID: String, userID: String): Status
}