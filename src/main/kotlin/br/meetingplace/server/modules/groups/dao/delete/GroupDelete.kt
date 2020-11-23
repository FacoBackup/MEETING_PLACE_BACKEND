package br.meetingplace.server.modules.groups.dao.delete

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.requests.generic.operators.SimpleOperator

class GroupDelete private constructor() {
    companion object {
        private val Class = GroupDelete()
        fun getClass() = Class
    }

    fun delete(data: SimpleOperator, communityDB: CommunityDBInterface, userDB: UserDBInterface, chatDB: ChatDBInterface, groupDB: GroupDBInterface) {
        val group = groupDB.select(data.identifier.ID)
        lateinit var members: List<String>
        lateinit var groups: List<String>
        lateinit var userGroups: List<String>

        when (data.identifier.community && !data.identifier.owner.isNullOrBlank()) {
            true -> {
                val community = communityDB.select(data.identifier.owner)
                if (group != null && community != null && group.getApproved() && userDB.check(data.login.email) && data.login.email in group.getModerators()) {
                    val chat = chatDB.select(group.getChatID())
                    members = group.getMembers()

                    for (element in members) {
                        val member = userDB.select(element)
                        val memberRole = group.getMemberRole(element)

                        if (member != null && memberRole != null) {
                            userGroups = member.getGroups()
                            userGroups.remove(group.getGroupID())
                            member.setGroups(userGroups)

                            userDB.insert(member)
                        }
                    }

                    groups = community.getGroups()
                    groups.remove(group.getGroupID())
                    community.setGroups(groups)

                    if (chat != null)
                        chatDB.delete(chat)

                    groupDB.delete(group)
                }
            }
            false -> {
                if (group != null && userDB.check(data.login.email) && data.login.email in group.getModerators()) {
                    val chat = chatDB.select(group.getChatID())
                    members = group.getMembers()

                    for (element in members) {
                        val member = userDB.select(element)
                        val memberRole = group.getMemberRole(element)
                        if (member != null && memberRole != null) {
                            userGroups = member.getGroups()
                            userGroups.remove(group.getGroupID())
                            member.setGroups(userGroups)
                        }
                    }

                    if (chat != null)
                        chatDB.delete(chat)

                    groupDB.delete(group)
                }
            }
        }
    }
}