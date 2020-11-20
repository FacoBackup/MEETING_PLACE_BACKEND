package br.meetingplace.server.modules.groups.dao.factory

import br.meetingplace.server.dto.notification.NotificationData
import br.meetingplace.server.dto.notification.types.NotificationMainType
import br.meetingplace.server.dto.notification.types.NotificationSubType
import br.meetingplace.server.dto.owner.OwnerType
import br.meetingplace.server.db.interfaces.ChatDBInterface
import br.meetingplace.server.db.interfaces.CommunityDBInterface
import br.meetingplace.server.db.interfaces.GroupDBInterface
import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.modules.chat.dto.Chat
import br.meetingplace.server.modules.chat.dto.ChatOwnerData
import br.meetingplace.server.modules.groups.dto.Group
import br.meetingplace.server.modules.groups.dto.GroupOwnerData
import br.meetingplace.server.routers.generic.requests.CreationData

class GroupFactory private constructor() {
    companion object {
        private val Class = GroupFactory()
        fun getClass() = Class
    }
    private fun getGroupID(name: String, creator: String): String{
            return (name.replace("\\s".toRegex(), "") + "_" + (creator.replaceAfter("@", "")).removeSuffix("@")).toLowerCase()
    }

    fun create(data: CreationData, rwGroup: GroupDBInterface, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface, rwChat: ChatDBInterface) {
        val user = rwUser.select(data.login.email)
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

                    rwGroup.insert(newGroup)
                    rwChat.insert(newChat)
                    rwUser.insert(user)
                }
                true -> {
                    val community = rwCommunity.select(data.identifier.ID)
                    if (community != null ) {
                        communityMods = community.getModerators()
                        notification = NotificationData(NotificationMainType.COMMUNITY, NotificationSubType.CREATION_REQUEST)
                        for (moderator in communityMods) {
                            val mod = rwUser.select(moderator)
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


                        rwGroup.insert(newGroup)
                        rwChat.insert(newChat)
                        rwUser.insert(user)
                        rwCommunity.insert(community)
                    }
                }
            }
        }
    }

}