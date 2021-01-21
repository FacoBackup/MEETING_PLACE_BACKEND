package br.meetingplace.server.modules.community.dto.requests

data class RequestCommunityCreation(val name: String,
                                    val pic: String?,
                                    val about: String,
                                    val relatedCommunityID: Long?,
                                    val background: String?
                                    )