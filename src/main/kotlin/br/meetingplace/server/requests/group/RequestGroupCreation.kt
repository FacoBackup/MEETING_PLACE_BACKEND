package br.meetingplace.server.requests.group

data class RequestGroupCreation(val name: String, val imageURL: String?,
                                val about: String, val communityID: String?,
                                val userID: String)