package br.meetingplace.server.modules.community.dao.member


import br.meetingplace.server.modules.community.dto.CommunityMemberDTO
import br.meetingplace.server.response.status.Status

interface CMI {
    fun create(data: CommunityMemberDTO): Status
    fun read(communityID: String, userID: String): CommunityMemberDTO?
    fun update(communityID: String, userID: String, role: String): Status
    fun delete(communityID: String, userID: String): Status
}