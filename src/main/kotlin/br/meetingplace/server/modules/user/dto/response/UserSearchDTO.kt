package br.meetingplace.server.modules.user.dto.response

data class UserSearchDTO (val email: String,
                          val name: String,
                          val imageURL: String?,
                          val isFollowing: Boolean,
                          val userID: Long
                          )
