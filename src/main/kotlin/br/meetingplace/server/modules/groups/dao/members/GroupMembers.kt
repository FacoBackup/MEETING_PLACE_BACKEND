package br.meetingplace.server.modules.groups.dao.members

import br.meetingplace.server.modules.notification.dto.NotificationData
import br.meetingplace.server.modules.notification.dto.types.NotificationMainType
import br.meetingplace.server.modules.notification.dto.types.NotificationSubType
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.requests.generic.MemberOperator

class GroupMembers private constructor() {
    companion object {
        private val Class = GroupMembers()
        fun getClass() = Class
    }

    fun addMember(data: MemberOperator, rwCommunity: CommunityDBInterface, rwGroup: GroupDBInterface, rwUser: UserDBInterface) {
        val user = rwUser.select(data.login.email)
        val newMember = rwUser.select(data.memberEmail)

        lateinit var notification: NotificationData
        lateinit var toBeAdded: MemberData

        if (user != null && newMember != null && !data.identifier.owner.isNullOrBlank()) {
            toBeAdded = MemberData(newMember.getEmail(), MemberType.NORMAL)
            when (data.identifier.community) {
                false -> {
                    val group = rwGroup.select(data.identifier.ID)
                    if (group != null && group.verifyMember(data.login.email)
                            && !group.verifyMember(data.memberEmail)) {

                        notification = NotificationData(NotificationMainType.GROUP, NotificationSubType.ADDED_IN_GROUP)

                        group.updateMember(toBeAdded, false)
                        newMember.updateMemberIn(group.getGroupID(), false)
                        newMember.updateInbox(notification)
                        rwUser.insert(newMember)
                        rwGroup.insert(group)
                    }
                }
                true -> {
                    val group = rwGroup.select(data.identifier.ID)
                    val community = rwCommunity.select(data.identifier.owner)
                    if (group != null && community != null && (data.login.email == group.getCreator() || data.login.email in community.getModerators())) {
                        notification = NotificationData(NotificationMainType.GROUP, NotificationSubType.ADDED_IN_GROUP)
                        group.updateMember(toBeAdded, false)
                        newMember.updateMemberIn(group.getGroupID(), false)
                        newMember.updateInbox(notification)
                        rwUser.insert(newMember)
                        rwGroup.insert(group)
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

        if (user != null && group != null && member != null &&
                (group.getMemberRole(user.getEmail()) == MemberType.MODERATOR || group.getMemberRole(user.getEmail()) == MemberType.CREATOR) &&
                group.getMemberRole(member.getEmail()) != null
        ) {

            when (data.stepDown) {
                true -> {
                    if (group.getMemberRole(member.getEmail()) == MemberType.MODERATOR)
                        group.updateMemberRole(member.getEmail(), MemberType.NORMAL)
                }
                false -> {
                    if (group.getMemberRole(member.getEmail()) == MemberType.NORMAL)
                        group.updateMemberRole(member.getEmail(), MemberType.MODERATOR)
                }
            }
        }
    }

    fun removeMember(data: MemberOperator, rwGroup: GroupDBInterface, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface) {

        val user = rwUser.select(data.login.email)
        val member = rwUser.select(data.memberEmail)

        lateinit var toBeRemoved: MemberData

        if (user != null && member != null && !data.identifier.owner.isNullOrBlank()) {
            when (data.identifier.community) {
                false -> {
                    val group = rwGroup.select(data.identifier.ID)


                    if (group != null && group.verifyMember(data.login.email) && (data.login.email in group.getModerators() || data.login.email in group.getCreator())) {
                        val memberRole = group.getMemberRole(member.getEmail())
                        if(memberRole != null){
                            toBeRemoved = MemberData(member.getEmail(), memberRole)
                            group.updateMember(toBeRemoved, true)
                            member.updateMemberIn(group.getGroupID(), true)

                            rwUser.insert(member)
                            rwGroup.insert(group)
                        }

                    }

                }
                true -> { //COMMUNITY
                    val group = rwGroup.select(data.identifier.ID)
                    val community = rwCommunity.select(data.identifier.owner)
                    if (group != null && community != null && (data.login.email == group.getCreator() || data.login.email in community.getModerators())) {

                        group.updateMember(toBeRemoved, true)
                        member.updateMemberIn(group.getGroupID(), true)
                        rwUser.insert(member)
                        rwGroup.insert(group)
                    }
                }
            }
        }
    } //UPDATE
}