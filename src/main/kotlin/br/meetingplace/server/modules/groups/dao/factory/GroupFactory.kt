package br.meetingplace.server.modules.groups.dao.factory

import br.meetingplace.server.modules.global.dto.notification.NotificationData
import br.meetingplace.server.modules.global.dto.notification.types.NotificationMainType
import br.meetingplace.server.modules.global.dto.notification.types.NotificationSubType
import br.meetingplace.server.modules.global.dto.owner.OwnerType
import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.dto.Chat
import br.meetingplace.server.modules.global.dto.owner.OwnerData
import br.meetingplace.server.modules.groups.dto.Group
import br.meetingplace.server.requests.generic.data.CreationData
import java.util.*

class GroupFactory private constructor() {

    companion object {
        private val Class = GroupFactory()
        fun getClass() = Class
    }

    fun create(data: CreationData, groupDB: GroupDBInterface, userDB: UserDBInterface, communityDB: CommunityDBInterface, chatDB: ChatDBInterface) {
        val user = userDB.select(data.login.email)
        lateinit var communityMods: List<String>
        lateinit var notification: NotificationData
        lateinit var newGroup: Group
        lateinit var groupID: String
        lateinit var groups: List<String>
        if (user != null && data.name.isNotEmpty() && groupID !in user.getMemberIn() && groupID !in user.getMyGroups()) {
            when (data.identifier.community) {
                false -> {
                    groupID = UUID.randomUUID().toString()
                    val newChat = Chat(UUID.randomUUID().toString(), OwnerData(groupID, OwnerType.GROUP))
                    newGroup = Group(owner = OwnerData(data.login.email, OwnerType.USER), chatID = newChat.getID(), ID = groupID, about = data.about, creator = data.login.email, name = data.name,imageURL = data.imageURL)

                    user.updateMyGroups(groupID, false)

                    groupDB.insert(newGroup)
                    chatDB.insert(newChat)
                    userDB.insert(user)
                }
                true -> {
                    val community = communityDB.select(data.identifier.ID)
                    if (community != null ) {
                        communityMods = community.getModerators()
                        notification = NotificationData(NotificationMainType.COMMUNITY, NotificationSubType.CREATION_REQUEST, community.getID())
                        for (moderator in communityMods) {
                            val mod = userDB.select(moderator)
                            if (mod != null && mod != user)
                                mod.updateInbox(notification)
                        }
                        groupID = UUID.randomUUID().toString()
                        val newChat = Chat(UUID.randomUUID().toString(), OwnerData(groupID, OwnerType.GROUP))
                        newGroup = Group(owner = OwnerData(community.getID(), OwnerType.COMMUNITY), chatID = newChat.getID(), ID = groupID, about = data.about, creator = data.login.email, name = data.name ,imageURL = data.imageURL)
                        user.updateMyGroups(groupID, false)

                        if (data.login.email !in community.getModerators()){
                            groups = community.getGroupsInValidation()
                            groups.add(newGroup.getGroupID())

                            community.setGroupsInValidation(groups)
                        }
                        else{
                            groups = community.getApprovedGroups()
                            groups.add(newGroup.getGroupID())

                            community.setApprovedGroups(groups)
                        }

                        groupDB.insert(newGroup)
                        chatDB.insert(newChat)
                        userDB.insert(user)
                        communityDB.insert(community)
                    }
                }
            }
        }
    }

}