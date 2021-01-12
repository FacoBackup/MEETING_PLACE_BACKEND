package br.meetingplace.server.modules.community.dto.response

data class CommunityRelatedUsersDTO(
    val communityID: String,
    val userID: String,
    val userName: String,
    val userImageURL: String?,
    val userEmail: String,
    val role: String,
    val communityName: String?,
    val affiliatedCommunityID: String?
)
