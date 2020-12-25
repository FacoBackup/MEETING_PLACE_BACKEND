package br.meetingplace.server.modules.user.dto.response

data class UserSocialDTO (val email: String,
                          val name: String,
                          val imageURL: String?,
                          val bornDate: Long)