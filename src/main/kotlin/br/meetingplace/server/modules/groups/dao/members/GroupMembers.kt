package br.meetingplace.server.modules.groups.dao.members

import br.meetingplace.server.modules.global.dto.notification.NotificationData
import br.meetingplace.server.modules.global.dto.notification.types.NotificationMainType
import br.meetingplace.server.modules.global.dto.notification.types.NotificationSubType
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.requests.generic.operators.MemberOperator

class GroupMembers private constructor() {
    companion object {
        private val Class = GroupMembers()
        fun getClass() = Class
    }

    fun addMember(data: MemberOperator, communityDB: CommunityDBInterface, groupDB: GroupDBInterface, userDB: UserDBInterface) {
        val user = userDB.select(data.login.email)
        val newMember = userDB.select(data.memberEmail)
        lateinit var notification: NotificationData

        if (user != null && newMember != null && !data.identifier.owner.isNullOrBlank()) {
            when (data.identifier.community) {
                false -> {
                    val group = groupDB.select(data.identifier.ID)
                    if (group != null && group.verifyMember(data.login.email)
                            && !group.verifyMember(data.memberEmail)) {

                        notification = NotificationData(NotificationMainType.GROUP, NotificationSubType.ADDED_IN_GROUP, group.getGroupID())

                        group.updateMember(newMember.getEmail(),MemberType.NORMAL, false)
                        newMember.updateMemberIn(group.getGroupID(), false)
                        newMember.updateInbox(notification)
                        userDB.insert(newMember)
                        groupDB.insert(group)
                    }
                }
                true -> {
                    val group = groupDB.select(data.identifier.ID)
                    val community = communityDB.select(data.identifier.owner)
                    if (group != null && community != null && data.login.email in community.getModerators()) {
                        notification = NotificationData(NotificationMainType.GROUP, NotificationSubType.ADDED_IN_GROUP, group.getGroupID())
                        group.updateMember(newMember.getEmail(), MemberType.NORMAL,false)
                        newMember.updateMemberIn(group.getGroupID(), false)
                        newMember.updateInbox(notification)
                        userDB.insert(newMember)
                        groupDB.insert(group)
                    }
                }
            }
        }
    }

    fun changeMemberRole(data: MemberOperator, rwGroup: GroupDBInterface, rwUser: UserDBInterface) {
        val user = rwUser.select(data.login.email)
        val member = rwUser.select(data.memberEmail)

        val group = when (data.identifier.community) {
            false -> data.identifier.owner?.let { rwGroup.select(data.identifier.ID) }
            true -> data.identifier.owner?.let { rwGroup.select(data.identifier.ID) }
        }

        if (user != null && group != null && member != null && group.getMemberRole(user.getEmail()) == MemberType.MODERATOR) {
            when (data.stepDown) {
                true -> if (group.getMemberRole(member.getEmail()) == MemberType.MODERATOR)
                        group.updateMemberRole(member.getEmail(), MemberType.NORMAL)

                false -> if (group.getMemberRole(member.getEmail()) == MemberType.NORMAL)
                        group.updateMemberRole(member.getEmail(), MemberType.MODERATOR)
            }
        }
    }

    fun removeMember(data: MemberOperator, groupDB: GroupDBInterface, userDB: UserDBInterface, communityDB: CommunityDBInterface) {

        val user = userDB.select(data.login.email)
        val member = userDB.select(data.memberEmail)
        val group = groupDB.select(data.identifier.ID)

        if (user != null && member != null && group != null && !data.identifier.owner.isNullOrBlank()) {
            when (data.identifier.community) {
                false -> {
                    if ( group.verifyMember(data.login.email) && data.login.email in group.getModerators()) {
                        val memberRole = group.getMemberRole(member.getEmail())
                        if(memberRole != null){
                            group.updateMember(member.getEmail(), memberRole,true)
                            member.updateMemberIn(group.getGroupID(), true)

                            userDB.insert(member)
                            groupDB.insert(group)
                        }
                    }
                }
                true -> {
                    val community = communityDB.select(data.identifier.owner)

                    if (community != null && data.login.email in community.getModerators() && group.getGroupID() in community.getApprovedGroups()) {
                        val memberRole = community.getMemberRole(member.getEmail())
                        if (memberRole != null)
                        group.updateMember(member.getEmail(),memberRole,true)
                        member.updateMemberIn(group.getGroupID(), true)
                        userDB.insert(member)
                        groupDB.insert(group)
                    }
                }
            }
        }
    }
}