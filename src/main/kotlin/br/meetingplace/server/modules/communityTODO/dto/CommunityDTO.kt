package br.meetingplace.server.modules.communityTODO.dto

data class CommunityDTO (val name: String, val id: String,
                         val about: String?, val imageURL: String?,
                         val creationDate: String, val parentCommunityID: String?,
                         val location: String)