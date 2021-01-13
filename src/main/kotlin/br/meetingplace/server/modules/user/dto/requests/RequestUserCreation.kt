package br.meetingplace.server.modules.user.dto.requests

data class RequestUserCreation(val userName: String,
                               val gender: String,
                               val nationality: String?,
                               val birthDate: Long,
                               val cityOfBirth: String?,
                               val phoneNumber: String,
                               val email: String,
                               val password: String,
                               val admin: Boolean)