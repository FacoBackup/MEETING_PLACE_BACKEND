package br.meetingplace.server.modules.community.dto.response

data class CommunityDTO (val name: String,
                         val id: Long,
                         val about: String?,
                         val imageURL: String?,
                         val creationDate: Long,
                         val parentCommunityID: Long?,
                         val backgroundImageURL: String?
                         )