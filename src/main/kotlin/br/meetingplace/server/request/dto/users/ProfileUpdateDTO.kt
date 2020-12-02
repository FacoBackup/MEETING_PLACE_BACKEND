package br.meetingplace.server.request.dto.users

data class ProfileUpdateDTO(val imageURL: String?, val about: String?,
                            val userID: String, val name: String?,
                            val nationality: String?, val phoneNumber: String?,
                            val city: String?)