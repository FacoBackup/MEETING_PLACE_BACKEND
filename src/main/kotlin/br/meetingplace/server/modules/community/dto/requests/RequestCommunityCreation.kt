package br.meetingplace.server.modules.community.dto.requests

data class RequestCommunityCreation(val name: String,
                                    val imageURL: String?,
                                    val about: String,
                                    val parentCommunityID: String?
                                    )