package br.meetingplace.server.modules.user.dto.response

data class UserSimplifiedDTO (val email: String,
                              val name: String,
                              val imageURL: String?,
                              val birthDate: Long,
                              val backgroundImageURL: String?
                              )