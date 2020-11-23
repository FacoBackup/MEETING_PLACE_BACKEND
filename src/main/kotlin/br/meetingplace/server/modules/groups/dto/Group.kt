package br.meetingplace.server.modules.groups.dto

import br.meetingplace.server.modules.global.dto.owner.OwnerData
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType

class Group(creator: String, private val owner: OwnerData, private val ID: String,
            private var name: String, private val chatID: String, private var about: String?,
            private var imageURL: String?, private var approved: Boolean){

    private var members = mutableListOf<MemberData>()

    init {
        members.add(MemberData(creator, MemberType.MODERATOR))
    }

    fun getMembers() = members
    fun getApproved() = approved
    fun getImageURL() = imageURL
    fun getOwner() = owner
    fun getNameGroup() = name
    fun getID() = ID
    fun getChatID() = chatID

    fun setMembers(members: List<MemberData>){
        this.members = members as MutableList<MemberData>
    }
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
