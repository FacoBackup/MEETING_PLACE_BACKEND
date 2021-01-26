package br.meetingplace.server.modules.community.dto.requests

data class CommunityUpdateDTO(
    val communityID: Long,
    val about: String?,
    val imageURL: String?,
    val name: String?,
    val backgroundImageURL: String?
)
