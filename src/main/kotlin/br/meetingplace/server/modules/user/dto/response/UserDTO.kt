package br.meetingplace.server.modules.user.dto.response

data class UserDTO (val email: String,
                    val name: String,
                    val gender: String,
                    val admin: Boolean,
                    val birthDate: String?,
                    val imageURL: String?,
                    val about: String?,
                    val cityOfBirth: String,
                    val phoneNumber: String?,
                    val nationality: String)