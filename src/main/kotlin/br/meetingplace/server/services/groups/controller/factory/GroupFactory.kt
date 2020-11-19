package br.meetingplace.server.services.groups.controller.factory

import br.meetingplace.server.loadstore.interfaces.ChatLSInterface
import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.GroupLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.CreationData
import br.meetingplace.server.services.chat.classes.Chat
import br.meetingplace.server.services.groups.classes.Group
import br.meetingplace.server.data.classes.notification.NotificationData
import br.meetingplace.server.data.classes.notification.data.NotificationMainType
import br.meetingplace.server.data.classes.notification.data.NotificationSubType
import br.meetingplace.server.data.classes.owner.OwnerType
import br.meetingplace.server.data.classes.owner.chat.ChatOwnerData
import br.meetingplace.server.data.classes.owner.group.GroupOwnerData

class GroupFactory private constructor() {
    companion object {
        private val Class = GroupFactory()
        fun getClass() = Class
    }
    private fun getGroupID(name: String, creator: String): String{
            return (name.replace("\\s".toRegex(), "") + "_" + (creator.replaceAfter("@", "")).removeSuffix("@")).toLowerCase()
    }

    fun create(data: CreationData, rwGroup: GroupLSInterface, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface, rwChat: ChatLSInterface) {
        val user = rwUser.load(data.login.email)
        lateinit var communityMods: List<String>
        lateinit var notification: NotificationData
        lateinit var newGroup: Group
        val groupID = getGroupID(data.name, data.login.email)

        if (user != null && data.name.isNotEmpty() && groupID !in user.getMemberIn() && groupID !in user.getMyGroups()) {
            when (data.identifier.community) {
                false -> {
                    val newChat = Chat(groupID + "_CHAT", ChatOwnerData(user.getEmail(), groupID, OwnerType.USER, OwnerType.GROUP))
                    newGroup = Group(GroupOwnerData(data.login.email, data.login.email, OwnerType.USER), groupID, data.name, newChat.getID())
                    user.updateMyGroups(groupID, false)

                    rwGroup.write(newGroup)
                    rwChat.store(newChat)
                    rwUser.store(user)
                }
                true -> {
                    val community = rwCommunity.load(data.identifier.ID)
                    if (community != null ) {
                        communityMods = community.getModerators()
                        notification = NotificationData(NotificationMainType.COMMUNITY, NotificationSubType.CREATION_REQUEST)
                        for (moderator in communityMods) {
                            val mod = rwUser.load(moderator)
                            if (mod != null && mod != user)
                                mod.updateInbox(notification)
                        }
                        val newChat = Chat(groupID + "_CHAT", ChatOwnerData(community.getID(), groupID, OwnerType.COMMUNITY, OwnerType.GROUP))
                        newGroup = Group(GroupOwnerData(community.getID(), data.login.email, OwnerType.COMMUNITY), groupID, data.name, newChat.getID())

                        user.updateMyGroups(groupID, false)

                        if (data.login.email !in community.getModerators())
                            community.updateGroupsInValidation(newGroup.getGroupID(), null)
                        else if (data.login.email in community.getModerators() || data.login.email in community.getCreator())
                            community.updateGroupsInValidation(newGroup.getGroupID(), true)


                        rwGroup.write(newGroup)
                        rwChat.store(newChat)
                        rwUser.store(user)
                        rwCommunity.store(community)
                    }
                }
            }
        }
    }

}