package br.meetingplace.server.modules.community.dto.response

data class CommunityInfoDTO(val name: String,
                            val communityID: String,
                            val about: String?,
                            val imageURL: String?,
                            val creationDate: Long,
                            val parentCommunityID: String?,
                            val backgroundImageURL: String?,
                            val role: String?,
                            val parentCommunityName: String?,
                            val parentCommunityImageURL: String?
                            )
