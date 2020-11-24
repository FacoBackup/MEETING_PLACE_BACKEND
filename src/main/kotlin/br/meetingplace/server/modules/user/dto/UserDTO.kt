package br.meetingplace.server.modules.user.dto

data class UserDTO (val id: String, val name: String, val email: String,
                    val gender: String, val birthDate: String, val imageURL: String?,
                    val about: String?, val cityOfBirth: String, val phoneNumber: String)