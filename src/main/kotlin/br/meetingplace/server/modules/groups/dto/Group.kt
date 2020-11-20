package br.meetingplace.server.modules.groups.dto

import br.meetingplace.server.modules.members.dao.Members
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType

class Group(private val owner: GroupOwnerData, private val groupID: String, private val name: String, private val chatID: String) : Members() {
    private var about: String? = null

    fun getOwner() = owner
    fun getNameGroup() = name
    fun getGroupID() = groupID
    fun getChatID() = chatID

    init {
        startMembers(MemberData(owner.groupCreatorID, MemberType.CREATOR))
    }

    fun updateAbout(about: String) {
        this.about = about
    }
}
