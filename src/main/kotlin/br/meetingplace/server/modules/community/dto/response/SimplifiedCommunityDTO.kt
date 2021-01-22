package br.meetingplace.server.modules.community.dto.response

data class SimplifiedCommunityDTO( val name: String,
                                   val communityID: Long,
                                   val about: String?,
                                   val imageURL: String?)
