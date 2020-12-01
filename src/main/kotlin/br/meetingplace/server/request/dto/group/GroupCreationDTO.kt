package br.meetingplace.server.request.dto.group

data class GroupCreationDTO(val name: String, val imageURL: String?,
                            val about: String, val communityID: String?,
                            val userID: String)