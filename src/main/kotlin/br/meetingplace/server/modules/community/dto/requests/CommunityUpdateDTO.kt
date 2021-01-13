package br.meetingplace.server.modules.community.dto.requests

data class CommunityUpdateDTO(
    val communityID: String,
    val about: String?,
    val imageURL: String?,
    val backgroundImageURL: String?
)
