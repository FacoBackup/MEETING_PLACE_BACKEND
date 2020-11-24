package br.meetingplace.server.modules.community.classes

import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime


class Community(private var name: String, private val id: String, private var about: String?,
                private var imageURL: String?,private val creationDate: LocalDateTime): Table() {

    fun getCreationDate () = creationDate
    fun getName() = name
    fun getID() = id

    fun setName(name: String){
        this.name = name
    }
    fun setImageURL(imageURL: String?) {
        this.imageURL = imageURL
    }
    fun setAbout(about: String?) {
        this.about = about
    }
}