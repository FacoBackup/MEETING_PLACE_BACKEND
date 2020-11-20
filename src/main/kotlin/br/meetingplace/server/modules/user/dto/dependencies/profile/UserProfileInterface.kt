package br.meetingplace.server.modules.user.dto.dependencies.profile

interface UserProfileInterface {
    fun updateProfile(about: String?, nationality: String?, gender: String?)
    fun getGender(): String?
    fun getNationality(): String?
    fun getAbout(): String?
    fun getImageURL(): String?
}