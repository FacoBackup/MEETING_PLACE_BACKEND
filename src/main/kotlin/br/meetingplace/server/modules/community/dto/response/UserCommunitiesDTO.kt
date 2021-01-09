package br.meetingplace.server.modules.community.dto.response

data class UserCommunitiesDTO(
    val name: String,
    val about: String?,
    val communityID: String,
    val imageURL: String?,
    val relatedCommunityName: String?, val role: String)
