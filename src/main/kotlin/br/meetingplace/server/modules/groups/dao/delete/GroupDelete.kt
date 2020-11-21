package br.meetingplace.server.modules.groups.dao.delete

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.dto.Chat
import br.meetingplace.server.requests.generic.operators.SimpleOperator

class GroupDelete private constructor() {
    companion object {
        private val Class = GroupDelete()
        fun getClass() = Class
    }

    fun delete(data: SimpleOperator, communityDB: CommunityDBInterface, userDB: UserDBInterface, chatDB: ChatDBInterface, groupDB: GroupDBInterface){

        val user = userDB.select(data.login.email)
        val group = groupDB.select(data.identifier.ID)
        lateinit var members: List<String>
        lateinit var groups: List<String>

        when(data.identifier.community){
            true->{
                val community = data.identifier.owner?.let { communityDB.select(it) }
                if(group != null && community != null && user != null && user.getEmail() in group.getModerators()){
                    val chat = chatDB.select(group.getChatID())
                    members = group.getMembers()

                    for (element in members) {
                        val member = userDB.select(element)
                        if (member != null) {
                            member.updateMemberIn(group.getGroupID(), true)
                            userDB.insert(member)
                        }
                    }

                    groups = community.getGroupsInValidation()
                    groups.remove(group.getGroupID())
                    community.setGroupsInValidation(groups)

                    groups = community.getApprovedGroups()
                    groups.remove(group.getGroupID())
                    community.setApprovedGroups(groups)

                    if(chat != null)
                        chatDB.delete(chat)

                    groupDB.delete(group)
                    userDB.insert(user)
                }
            }
            false->{
                if(group != null && user != null && user.getEmail() in group.getModerators()){
                    val chat = chatDB.select(group.getChatID())
                    members = group.getMembers()

                    for (element in members) {
                        val member = userDB.select(element)
                        if (member != null) {
                            member.updateMemberIn(group.getGroupID(), true)
                            userDB.insert(member)
                        }
                    }

                    if(chat != null)
                        chatDB.delete(chat)

                    groupDB.delete(group)
                    userDB.insert(user)
                }
            }
        }
    }
}