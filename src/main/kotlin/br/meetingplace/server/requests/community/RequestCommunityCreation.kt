package br.meetingplace.server.requests.community

data class RequestCommunityCreation(val name: String, val imageURL: String?,
                                    val about: String, val communityID: String?,
                                    val userID: String, val location: String)