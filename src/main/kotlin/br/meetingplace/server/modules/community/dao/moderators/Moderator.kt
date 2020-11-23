package br.meetingplace.server.modules.community.dao.moderators

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.global.methods.member.getMemberRole
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.requests.community.Approval
import br.meetingplace.server.requests.generic.operators.MemberOperator

class Moderator private constructor() {

    companion object {
        private val Class = Moderator()
        fun getClass() = Class
    }

    fun approveTopic(data: Approval, communityDB: CommunityDBInterface, userDB: UserDBInterface, topicDB: TopicDBInterface):Status {
        val user = userDB.select(data.login.email)
        val community = data.identifier.owner?.let { communityDB.select(it) }
        val topic = topicDB.select(data.identifier.ID, null)

        lateinit var topics: List<String>
        return if (user != null && community != null && getMemberRole(community.getMembers(), data.login.email) == MemberType.MODERATOR && !data.identifier.owner.isNullOrBlank() && topic != null && !topic.getApproved()) {

            topics = community.getTopics()
            topics.add(topic.getID())
            topic.setApproved(true)
            community.setTopics(topics)
            return communityDB.insert(community)
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

    fun approveGroup(data: Approval, communityDB: CommunityDBInterface, userDB: UserDBInterface, groupDB: GroupDBInterface): Status {
        val user = userDB.select(data.login.email)
        val community = data.identifier.owner?.let { communityDB.select(it) }
        val group = groupDB.select(data.identifier.ID)
        lateinit var groups: List<String>

        return if (user != null && community != null && getMemberRole(community.getMembers(),data.login.email) == MemberType.MODERATOR && group != null && !group.getApproved()) {

            groups = community.getGroups()
            groups.add(group.getID())
            group.setApproved(true)
            community.setTopics(groups)

            return communityDB.insert(community)
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }
}