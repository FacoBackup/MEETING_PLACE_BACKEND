package br.meetingplace.server.modules.community.dto.response

data class CommunityDTO (val name: String,
                         val id: String,
                         val about: String?,
                         val imageURL: String?,
                         val creationDate: Long,
                         val relatedCommunityID: String?,
                         val backgroundImageURL: String?
                         )