package br.meetingplace.server.modules.user.dto.response

data class UserDTO (val email: String,
                    val userName: String,
                    val category: String?,
                    val name: String,
                    val gender: String,
                    val birthDate:Long,
                    val imageURL: String?,
                    val about: String?,
                    val cityOfBirth: String?,
                    val phoneNumber: String?,
                    val nationality: String?,
                    val backgroundImageURL: String?,
                    val joinedIn: Long,
                    val id: Long
                )