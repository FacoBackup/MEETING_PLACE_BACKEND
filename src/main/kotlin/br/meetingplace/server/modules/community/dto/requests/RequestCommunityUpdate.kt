package br.meetingplace.server.modules.community.dto.requests

data class RequestCommunityUpdate(val name: String?,
                                  val about: String?,
                                  val parentCommunityID: String?,
                                  val communityID: String,
                                  val imageURL: String?,
                                  val backgroundImageURL: String?
                                )
