package br.meetingplace.server.modules.user.dto.response

data class UserSimplifiedDTO (val email: String,
                              val userID: Long,
                              val name: String,
                              val userName: String,
                              val imageURL: String?,
                              val birthDate: Long,
                              val backgroundImageURL: String?
                              )