package br.meetingplace.server.modules.groups.dao.delete

import br.meetingplace.server.db.interfaces.ChatDBInterface
import br.meetingplace.server.db.interfaces.CommunityDBInterface
import br.meetingplace.server.db.interfaces.GroupDBInterface
import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.routers.generic.requests.SimpleOperator

class GroupDelete private constructor() {
    companion object {
        private val Class = GroupDelete()
        fun getClass() = Class
    }

    fun delete(data: SimpleOperator, rwCommunity: CommunityDBInterface, rwUser: UserDBInterface, rwChat: ChatDBInterface, rwGroup: GroupDBInterface) {
        val user = rwUser.select(data.login.email)
        val group = rwGroup.select(data.identifier.ID)

        lateinit var members: List<String>

        if (user != null && group != null && data.login.email == group.getCreator() && !data.identifier.owner.isNullOrBlank()) {
            val chat = rwChat.select(group.getChatID())
            when (data.identifier.community) {
                false -> { // USER
                        members = group.getMembers()
                        for (element in members) {
                            val member = rwUser.select(element)
                            if (member != null) {
                                member.updateMemberIn(group.getGroupID(), true)
                                rwUser.insert(member)
                            }
                        }
                        user.updateMyGroups(group.getGroupID(), true)

                        if(chat != null)
                            rwChat.delete(chat)
                        rwGroup.delete(group)
                        rwUser.insert(user)
                }
                true -> { // COMMUNITY
                    val community = rwCommunity.select(data.identifier.owner)

                    if (community != null) {
                        when (community.checkGroupApproval(group.getGroupID())) {
                            true -> { // APPROVED

                                members = group.getMembers()
                                for (element in members) {
                                    val member = rwUser.select(element)
                                    if (member != null) {
                                        member.updateMemberIn(group.getGroupID(), true)
                                        rwUser.insert(member)
                                    }
                                }

                                community.removeApprovedGroup(group.getGroupID())
                                user.updateMyGroups(group.getGroupID(), true)
                                rwGroup.delete(group)
                                rwUser.insert(user)
                                rwCommunity.insert(community)
                            }
                            false -> { //IN VALIDATION

                                members = group.getMembers()
                                for (element in members) {
                                    val member = rwUser.select(element)
                                    if (member != null) {
                                        member.updateMemberIn(group.getGroupID(), true)
                                        rwUser.insert(member)
                                    }
                                }

                                community.updateGroupsInValidation(group.getGroupID(), false)
                                user.updateMyGroups(group.getGroupID(), true)

                                if(chat != null)
                                    rwChat.delete(chat)
                                rwGroup.delete(group)
                                rwUser.insert(user)
                                rwCommunity.insert(community)
                            }//false
                        }//when
                    }//if
                }//false
            }//when
        }//if
    }//method
}