package br.meetingplace.server.modules.groups.dao.factory

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.dto.Chat
import br.meetingplace.server.modules.global.dto.notification.NotificationData
import br.meetingplace.server.modules.global.dto.notification.types.NotificationMainType
import br.meetingplace.server.modules.global.dto.notification.types.NotificationSubType
import br.meetingplace.server.modules.global.dto.owner.OwnerData
import br.meetingplace.server.modules.global.dto.owner.OwnerType
import br.meetingplace.server.modules.global.methods.member.getMemberRole
import br.meetingplace.server.modules.groups.dto.Group
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.requests.generic.data.CreationData
import java.util.*

class GroupFactory private constructor() {

    companion object {
        private val Class = GroupFactory()
        fun getClass() = Class
    }

    fun create(data: CreationData, groupDB: GroupDBInterface, userDB: UserDBInterface, communityDB: CommunityDBInterface, chatDB: ChatDBInterface) {
        val user = userDB.select(data.login.email)
        lateinit var members: List<MemberData>
        lateinit var notification: NotificationData
        lateinit var newGroup: Group
        lateinit var newChat: Chat
        lateinit var groupID: String
        lateinit var groups: List<String>
        lateinit var userInbox: List<NotificationData>
        if (user != null && data.name.isNotEmpty() && groupID !in user.getGroups()) {
            when (data.identifier.community) {
                false -> {
                    groupID = UUID.randomUUID().toString()
                    newChat = Chat(UUID.randomUUID().toString(), OwnerData(groupID, OwnerType.GROUP))
                    newGroup = Group(approved = true, owner = OwnerData(data.login.email, OwnerType.USER), chatID = newChat.getID(), ID = groupID, about = data.about, creator = data.login.email, name = data.name, imageURL = data.imageURL)

                    groups = user.getGroups()
                    groups.add(newGroup.getID())
                    user.setGroups(groups)

                    groupDB.insert(newGroup)
                    chatDB.insert(newChat)
                    userDB.insert(user)
                }
                true -> {
                    val community = communityDB.select(data.identifier.ID)
                    if (community != null) {

                        groupID = UUID.randomUUID().toString()
                        newChat = Chat(UUID.randomUUID().toString(), OwnerData(groupID, OwnerType.GROUP))
                        newGroup = Group(approved = getMemberRole(community.getMembers(), data.login.email) == MemberType.MODERATOR, owner = OwnerData(community.getID(), OwnerType.COMMUNITY), chatID = newChat.getID(), ID = groupID, about = data.about, creator = data.login.email, name = data.name, imageURL = data.imageURL)

                        members = community.getMembers()
                        notification = NotificationData(NotificationMainType.COMMUNITY, NotificationSubType.CREATION_REQUEST, community.getID())
                        for (i in members) {
                            val mod = userDB.select(i.ID)
                            if (mod != null && mod != user && i.role == MemberType.MODERATOR){
                                userInbox = mod.getInbox()
                                userInbox.add(notification)
                                mod.setInbox(userInbox)
                            }

                        }

                        groups = community.getGroups()
                        groups.add(newGroup.getID())
                        community.setGroups(groups)

                        groups = user.getGroups()
                        groups.add(newGroup.getID())
                        user.setGroups(groups)

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