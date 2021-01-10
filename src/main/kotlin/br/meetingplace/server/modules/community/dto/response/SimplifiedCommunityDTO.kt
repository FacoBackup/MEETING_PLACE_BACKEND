package br.meetingplace.server.modules.community.dto.response

data class SimplifiedCommunityDTO(
        val role: String?,
        val name: String,
        val communityID: String,
        val about: String?,
        val imageURL: String?
)
