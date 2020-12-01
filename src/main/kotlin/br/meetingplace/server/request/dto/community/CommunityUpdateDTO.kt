package br.meetingplace.server.request.dto.community

data class CommunityUpdateDTO(val name: String?, val about: String?, val parentCommunityID: String?, val userID: String, val communityID: String)
