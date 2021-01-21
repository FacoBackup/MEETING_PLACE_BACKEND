package br.meetingplace.server.modules.user.dto.response

data class UserProfileDTO(
    val email: String,
    val name: String,
    val gender: String,
    val birthDate:Long,
    val imageURL: String?,
    val about: String?,
    val cityOfBirth: String?,
    val phoneNumber: String?,
    val nationality: String?,
    val backgroundImageURL: String?,
    val followers: Long,
    val following: Long,
    val topics: Long,
    val joinedIn: Long
)
