package br.meetingplace.server.services.community.controller.moderators

import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.GroupLSInterface
import br.meetingplace.server.loadstore.interfaces.TopicLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.MemberOperator
import br.meetingplace.server.dto.community.ApprovalData
import br.meetingplace.server.services.members.data.MemberType
import br.meetingplace.server.services.topic.classes.SimplifiedTopic

class Moderator private constructor() {

    companion object {
        private val Class = Moderator()
        fun getClass() = Class
    }

    fun approveTopic(data: ApprovalData, rwCommunity: CommunityLSInterface, rwUser: UserLSInterface, rwTopic: TopicLSInterface) {
        val user = rwUser.load(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.load(it) }

        if (user != null && community != null && data.login.email in community.getModerators() && !data.identifier.owner.isNullOrBlank()) {
            val topic = rwTopic.load(data.identifier.ID, null)
            if (topic != null) {
                community.updateTopicInValidation(SimplifiedTopic(data.identifier.ID, topic.getOwner()), true)
                rwCommunity.store(community)
            }
        }
    }

    fun approveGroup(data: ApprovalData, rwCommunity: CommunityLSInterface, rwUser: UserLSInterface, rwGroup: GroupLSInterface) {
        val user = rwUser.load(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.load(it) }

        if (user != null && community != null && (community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {
            val group = rwGroup.read(data.identifier.ID)
            if (group != null) {
                community.updateGroupsInValidation(data.identifier.ID, true)
                rwCommunity.store(community)
            }
        }
    }

    fun stepDown(data: MemberOperator, rwCommunity: CommunityLSInterface, rwUser: UserLSInterface) {
        val user = rwUser.load(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.load(it) }

        if (user != null && community != null && community.verifyMember(data.login.email))
            community.updateModerator(data.login.email, data.login.email, null)
    }
}