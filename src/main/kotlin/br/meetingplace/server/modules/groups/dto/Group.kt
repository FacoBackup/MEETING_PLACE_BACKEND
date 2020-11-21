package br.meetingplace.server.modules.groups.dto

import br.meetingplace.server.modules.members.dao.Members
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.modules.global.dto.owner.OwnerData

class Group(creator: String, private val owner: OwnerData, private val ID: String,
            private var name: String, private val chatID: String, private var about: String?,
            private var imageURL: String?) : Members() {

    init {
        updateMember(creator, MemberType.MODERATOR, false)
    }

    fun getOwner() = owner
    fun getNameGroup() = name
    fun getGroupID() = ID
    fun getChatID() = chatID

    fun setName(name: String){
        this.name = name
    }

    fun setAbout(about: String) {
        this.about = about
    }
}
