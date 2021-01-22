package br.meetingplace.server.modules.community.dto.response

data class SimplifiedUserCommunityDTO(
        val role: String?,
        val name: String,
        val communityID: Long,
        val about: String?,
        val imageURL: String?
)
