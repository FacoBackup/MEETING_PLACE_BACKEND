package br.meetingplace.server.modules.groups.dao.delete

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.requests.generic.operators.SimpleOperator

class GroupDelete private constructor() {
    companion object {
        private val Class = GroupDelete()
        fun getClass() = Class
    }

    fun delete(data: SimpleOperator, communityDB: CommunityDBInterface, userDB: UserDBInterface, chatDB: ChatDBInterface, groupDB: GroupDBInterface) {
        val user = userDB.select(data.login.email)
        val group = groupDB.select(data.identifier.ID)

        lateinit var members: List<String>

        if (user != null && group != null && data.login.email == group.getCreator() && !data.identifier.owner.isNullOrBlank()) {
            val chat = chatDB.select(group.getChatID())
            when (data.identifier.community) {
                false -> { // USER
                        members = group.getMembers()
                        for (element in members) {
                            val member = userDB.select(element)
                            if (member != null) {
                                member.updateMemberIn(group.getGroupID(), true)
                                userDB.insert(member)
                            }
                        }
                        user.updateMyGroups(group.getGroupID(), true)

                        if(chat != null)
                            chatDB.delete(chat)
                        groupDB.delete(group)
                        userDB.insert(user)
                }
                true -> { // COMMUNITY
                    val community = communityDB.select(data.identifier.owner)

                    if (community != null) {
                        when (community.checkGroupApproval(group.getGroupID())) {
                            true -> { // APPROVED

                                members = group.getMembers()
                                for (element in members) {
                                    val member = userDB.select(element)
                                    if (member != null) {
                                        member.updateMemberIn(group.getGroupID(), true)
                                        userDB.insert(member)
                                    }
                                }

                                community.removeApprovedGroup(group.getGroupID())
                                user.updateMyGroups(group.getGroupID(), true)
                                groupDB.delete(group)
                                userDB.insert(user)
                                communityDB.insert(community)
                            }
                            false -> { //IN VALIDATION

                                members = group.getMembers()
                                for (element in members) {
                                    val member = userDB.select(element)
                                    if (member != null) {
                                        member.updateMemberIn(group.getGroupID(), true)
                                        userDB.insert(member)
                                    }
                                }

                                community.updateGroupsInValidation(group.getGroupID(), false)
                                user.updateMyGroups(group.getGroupID(), true)

                                if(chat != null)
                                    chatDB.delete(chat)
                                groupDB.delete(group)
                                userDB.insert(user)
                                communityDB.insert(community)
                            }//false
                        }//when
                    }//if
                }//false
            }//when
        }//if
    }//method
}