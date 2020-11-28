package br.meetingplace.server.requests.users.data

data class UserCreationData(val userName: String, val gender: String,
                            val nationality: String, val birthDate: String,
                            val cityOfBirth: String, val phoneNumber: String,
                            val email: String)
//birthDate = dd-MM-yyyy