package br.meetingplace.server.modules.community.dao.moderators

import br.meetingplace.server.db.interfaces.CommunityDBInterface
import br.meetingplace.server.db.interfaces.GroupDBInterface
import br.meetingplace.server.db.interfaces.TopicDBInterface
import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.modules.topic.dto.SimplifiedTopic
import br.meetingplace.server.routers.community.requests.ApprovalData
import br.meetingplace.server.routers.generic.requests.MemberOperator

class Moderator private constructor() {

    companion object {
        private val Class = Moderator()
        fun getClass() = Class
    }

    fun approveTopic(data: ApprovalData, rwCommunity: CommunityDBInterface, rwUser: UserDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.select(it) }

        if (user != null && community != null && data.login.email in community.getModerators() && !data.identifier.owner.isNullOrBlank()) {
            val topic = rwTopic.select(data.identifier.ID, null)
            if (topic != null) {
                community.updateTopicInValidation(SimplifiedTopic(data.identifier.ID, topic.getOwner()), true)
                rwCommunity.insert(community)
            }
        }
    }

    fun approveGroup(data: ApprovalData, rwCommunity: CommunityDBInterface, rwUser: UserDBInterface, rwGroup: GroupDBInterface) {
        val user = rwUser.select(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.select(it) }

        if (user != null && community != null && (community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {
            val group = rwGroup.select(data.identifier.ID)
            if (group != null) {
                community.updateGroupsInValidation(data.identifier.ID, true)
                rwCommunity.insert(community)
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