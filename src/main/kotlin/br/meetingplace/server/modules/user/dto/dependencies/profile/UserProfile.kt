package br.meetingplace.server.modules.user.dto.dependencies.profile

class UserProfile private constructor() : UserProfileInterface {
    companion object {
        private val Class = UserProfile()
        fun getClass() = Class
    }

    private var gender: String? = null
    private var nationality: String? = null
    private var about: String? = null
    private var imageURL: String?= null
    override fun getImageURL() = imageURL
    override fun getGender(): String? {
        return gender
    }

    override fun getNationality(): String? {
        return nationality
    }

    override fun getAbout(): String? {
        return about
    }

    override fun updateProfile(about: String?, nationality: String?, gender: String?) {
        this.about = about
        this.nationality = nationality
        this.gender = gender
    }

}