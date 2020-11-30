package br.meetingplace.server.requests.users

data class RequestUserCreation(val userName: String, val gender: String,
                               val nationality: String, val birthDate: String,
                               val cityOfBirth: String, val phoneNumber: String,
                               val email: String)