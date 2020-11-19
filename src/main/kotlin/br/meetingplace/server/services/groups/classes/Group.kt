package br.meetingplace.server.services.groups.classes

import br.meetingplace.server.services.members.Members
import br.meetingplace.server.services.members.data.MemberData
import br.meetingplace.server.services.members.data.MemberType
import br.meetingplace.server.data.classes.owner.group.GroupOwnerData

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
