package br.meetingplace.server.modules.groups.dao.factory

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.classes.Chat
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.global.dto.notification.NotificationData
import br.meetingplace.server.modules.global.dto.notification.types.NotificationMainType
import br.meetingplace.server.modules.global.dto.notification.types.NotificationSubType
import br.meetingplace.server.modules.global.dto.owner.OwnerData
import br.meetingplace.server.modules.global.dto.owner.OwnerType
import br.meetingplace.server.modules.global.methods.member.getMemberRole
import br.meetingplace.server.modules.groups.db.Group
import br.meetingplace.server.modules.members.classes.MemberData
import br.meetingplace.server.modules.members.classes.MemberType
import br.meetingplace.server.requests.generic.data.CreationData
import java.util.*

object GroupFactory {

    fun create(data: CreationData, groupDB: GroupDBInterface, userDB: UserDBInterface, communityDB: CommunityDBInterface, chatDB: ChatDBInterface) : Status {
        val user = userDB.select(data.login.email)
        lateinit var members: List<MemberData>
        lateinit var notification: NotificationData
        lateinit var newGroup: Group
        lateinit var newChat: Chat
        lateinit var groupID: String
        lateinit var groups: List<String>
        lateinit var userInbox: List<NotificationData>
        return if (user != null && data.name.isNotEmpty() && groupID !in user.getGroups()) {
            return when (data.identifier.community) {
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
                    Status(statusCode = 200, StatusMessages.OK)
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

                        chatDB.insert(newChat)
                        userDB.insert(user)
                        communityDB.insert(community)
                        return groupDB.insert(newGroup)
                    }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                }
            }
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

}