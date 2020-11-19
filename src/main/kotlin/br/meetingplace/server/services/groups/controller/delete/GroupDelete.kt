package br.meetingplace.server.services.groups.controller.delete

import br.meetingplace.server.loadstore.interfaces.ChatLSInterface
import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.GroupLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.SimpleOperator

class GroupDelete private constructor() {
    companion object {
        private val Class = GroupDelete()
        fun getClass() = Class
    }

    fun delete(data: SimpleOperator, rwCommunity: CommunityLSInterface, rwUser: UserLSInterface, rwChat: ChatLSInterface, rwGroup: GroupLSInterface) {
        val user = rwUser.load(data.login.email)
        val group = rwGroup.read(data.identifier.ID)

        lateinit var members: List<String>

        if (user != null && group != null && data.login.email == group.getCreator() && !data.identifier.owner.isNullOrBlank()) {
            val chat = rwChat.load(group.getChatID())
            when (data.identifier.community) {
                false -> { // USER
                        members = group.getMembers()
                        for (element in members) {
                            val member = rwUser.load(element)
                            if (member != null) {
                                member.updateMemberIn(group.getGroupID(), true)
                                rwUser.store(member)
                            }
                        }
                        user.updateMyGroups(group.getGroupID(), true)

                        if(chat != null)
                            rwChat.delete(chat)
                        rwGroup.delete(group)
                        rwUser.store(user)
                }
                true -> { // COMMUNITY
                    val community = rwCommunity.load(data.identifier.owner)

                    if (community != null) {
                        when (community.checkGroupApproval(group.getGroupID())) {
                            true -> { // APPROVED

                                members = group.getMembers()
                                for (element in members) {
                                    val member = rwUser.load(element)
                                    if (member != null) {
                                        member.updateMemberIn(group.getGroupID(), true)
                                        rwUser.store(member)
                                    }
                                }

                                community.removeApprovedGroup(group.getGroupID())
                                user.updateMyGroups(group.getGroupID(), true)
                                rwGroup.delete(group)
                                rwUser.store(user)
                                rwCommunity.store(community)
                            }
                            false -> { //IN VALIDATION

                                members = group.getMembers()
                                for (element in members) {
                                    val member = rwUser.load(element)
                                    if (member != null) {
                                        member.updateMemberIn(group.getGroupID(), true)
                                        rwUser.store(member)
                                    }
                                }

                                community.updateGroupsInValidation(group.getGroupID(), false)
                                user.updateMyGroups(group.getGroupID(), true)

                                if(chat != null)
                                    rwChat.delete(chat)
                                rwGroup.delete(group)
                                rwUser.store(user)
                                rwCommunity.store(community)
                            }//false
                        }//when
                    }//if
                }//false
            }//when
        }//if
    }//method
}