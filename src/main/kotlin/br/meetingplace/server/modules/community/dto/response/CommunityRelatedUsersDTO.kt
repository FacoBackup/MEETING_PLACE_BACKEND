package br.meetingplace.server.modules.community.dto.response

data class CommunityRelatedUsersDTO(
    val communityID: Long,
    val userID: Long,
    val userName: String,
    val userImageURL: String?,
    val userEmail: String,
    val role: String,
    val communityName: String?,
    val affiliatedCommunityID: Long?
)
