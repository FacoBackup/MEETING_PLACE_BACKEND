package br.meetingplace.server.request.dto.users

data class UserCreationDTO(val userName: String, val gender: String,
                           val nationality: String, val birthDate: String,
                           val cityOfBirth: String, val phoneNumber: String,
                           val email: String)