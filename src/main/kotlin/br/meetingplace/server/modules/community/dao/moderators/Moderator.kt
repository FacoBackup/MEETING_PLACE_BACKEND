package br.meetingplace.server.modules.community.dao.moderators

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.requests.community.Approval
import br.meetingplace.server.requests.generic.operators.MemberOperator

class Moderator private constructor() {

    companion object {
        private val Class = Moderator()
        fun getClass() = Class
    }

    fun approveTopic(data: Approval, communityDB: CommunityDBInterface, userDB: UserDBInterface, topicDB: TopicDBInterface) {
        val user = userDB.select(data.login.email)
        val community = data.identifier.owner?.let { communityDB.select(it) }

        if (user != null && community != null && data.login.email in community.getModerators() && !data.identifier.owner.isNullOrBlank()) {
            val topic = topicDB.select(data.identifier.ID, null)
            if (topic != null) {
                community.updateTopicInValidation(SimplifiedTopic(data.identifier.ID, topic.getOwner()), true)
                communityDB.insert(community)
            }
        }
    }

    fun approveGroup(data: Approval, communityDB: CommunityDBInterface, userDB: UserDBInterface, groupDB: GroupDBInterface) {
        val user = userDB.select(data.login.email)
        val community = data.identifier.owner?.let { communityDB.select(it) }

        if (user != null && community != null && (community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {
            val group = groupDB.select(data.identifier.ID)
            if (group != null) {
                community.updateGroupsInValidation(data.identifier.ID, true)
                communityDB.insert(community)
            }
        }
    }

    fun stepDown(data: MemberOperator, rwCommunity: CommunityDBInterface, rwUser: UserDBInterface) {
        val user = rwUser.select(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.select(it) }

        if (user != null && community != null && community.verifyMember(data.login.email))
            community.updateModerator(data.login.email, data.login.email, null)
    }
}