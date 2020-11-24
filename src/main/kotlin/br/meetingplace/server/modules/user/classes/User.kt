package br.meetingplace.server.modules.user.classes

import org.jetbrains.exposed.sql.Table
import java.time.LocalDate
import java.time.LocalDateTime

class User(
        private var userName: String,
        private var birth: LocalDate,
        private var email: String,
        private var password: String,
        private val creationDate: LocalDateTime
): Table(){

    private var gender: String? = null
    private var nationality: String? = null
    private var about: String? = null
    private var imageURL: String? = null

    //GETTERS
    fun getImageURL() = imageURL
    fun getPassword() = password
    fun getBirthDate() = birth
    fun getEmail() = email
    fun getUserName() = userName
    fun getNationality() = nationality
    fun getAbout() = about
    fun getGender() = gender

    //SETTERS
    fun setImageURL(imageURL: String?){
        this.imageURL = imageURL
    }
    fun setAbout(about: String?) {
        this.about = about
    }
    fun setNationality(nationality: String?){
        this.nationality = nationality
    }
    fun setGender(gender: String?){
        this.gender = gender
    }
}