package br.meetingplace.server.user.classes.dependencies.profile

interface UserProfileInterface {
    fun updateProfile(about: String?, nationality: String?, gender: String?)
    fun getGender(): String?
    fun getNationality(): String?
    fun getAbout(): String?
}