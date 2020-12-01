package br.meetingplace.server.request.dto.community

data class CommunityCreationDTO(val name: String, val imageURL: String?,
                                val about: String, val communityID: String?,
                                val userID: String, val location: String)