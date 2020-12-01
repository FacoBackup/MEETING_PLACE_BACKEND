package br.meetingplace.server.request.dto.community

data class CommunityCreationDTO(val name: String, val imageURL: String?,
                                val about: String, val parentCommunityID: String?,
                                val userID: String, val location: String)