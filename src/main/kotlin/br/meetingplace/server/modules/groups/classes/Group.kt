package br.meetingplace.server.modules.groups.classes

import br.meetingplace.server.modules.global.dto.owner.OwnerData
import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime

class Group( private val owner: OwnerData, private val ID: String,
            private var name: String,private var about: String?,
            private var imageURL: String?, private var approved: Boolean): Table(){
    private val creationDate =  LocalDateTime.now()

    fun getCreationDate () = creationDate
    fun getApproved() = approved
    fun getImageURL() = imageURL
    fun getOwner() = owner
    fun getNameGroup() = name
    fun getID() = ID

    fun setName(name: String) {
        this.name = name
    }
    fun setApproved(approved: Boolean) {
        this.approved = approved
    }
    fun setImageURL(imageURL: String?) {
        this.imageURL = imageURL
    }
    fun setAbout(about: String) {
        this.about = about
    }
}
