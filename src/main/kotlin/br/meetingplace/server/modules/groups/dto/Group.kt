package br.meetingplace.server.modules.groups.dto

import br.meetingplace.server.modules.global.dto.owner.OwnerData
import br.meetingplace.server.modules.members.dao.Members
import br.meetingplace.server.modules.members.dto.MemberType

class Group(creator: String, private val owner: OwnerData, private val ID: String,
            private var name: String, private val chatID: String, private var about: String?,
            private var imageURL: String?, private var approved: Boolean) : Members() {

    init {
        addMember(creator, MemberType.MODERATOR)
    }

    fun getApproved() = approved
    fun getImageURL() = imageURL
    fun getOwner() = owner
    fun getNameGroup() = name
    fun getGroupID() = ID
    fun getChatID() = chatID

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
