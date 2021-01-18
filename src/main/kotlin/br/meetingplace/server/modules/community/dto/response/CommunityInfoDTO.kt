package br.meetingplace.server.modules.community.dto.response

data class CommunityInfoDTO(val name: String,
                            val communityID: String,
                            val about: String?,
                            val imageURL: String?,
                            val creationDate: Long,
                            val relatedCommunityID: String?,
                            val backgroundImageURL: String?,
                            val role: String?,
                            val relatedCommunityName: String?,
                            val relatedCommunityImageURL: String?,
                            val topics: Long,
                            val followers: Long,
                            val members: Long,
                            val mods: Long
                            )
