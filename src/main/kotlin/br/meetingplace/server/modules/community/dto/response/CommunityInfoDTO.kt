package br.meetingplace.server.modules.community.dto.response

data class CommunityInfoDTO(val name: String,
                            val communityID: Long,
                            val about: String?,
                            val pic: String?,
                            val creationDate: Long,
                            val background: String?,
                            val role: String?,
                            val mainCommunityID: Long?,
                            val mainCommunityName: String?,
                            val mainCommunityPic: String?,
                            val topics: Long,
                            val followers: Long,
                            val members: Long,
                            val mods: Long
                            )
