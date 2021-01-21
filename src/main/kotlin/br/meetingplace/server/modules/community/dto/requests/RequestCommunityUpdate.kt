package br.meetingplace.server.modules.community.dto.requests

data class RequestCommunityUpdate(val name: String?,
                                  val about: String?,
                                  val mainCommunityID: Long?,
                                  val communityID: Long,
                                  val imageURL: String?,
                                  val backgroundImageURL: String?
                                )
