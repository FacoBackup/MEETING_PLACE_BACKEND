package br.meetingplace.server.modules.group.dto.requests

data class RequestGroupCreation(val name: String,
                                val imageURL: String?,
                                val about: String,
                                val communityID: String?)