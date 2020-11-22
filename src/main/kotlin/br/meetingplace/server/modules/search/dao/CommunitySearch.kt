package br.meetingplace.server.modules.search.dao

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.groups.dto.Group
import br.meetingplace.server.modules.report.dto.Report
import br.meetingplace.server.modules.topic.dto.Topic
import br.meetingplace.server.requests.generic.operators.SimpleOperator

class CommunitySearch {

    fun seeReports(data: SimpleOperator, rwCommunity: CommunityDBInterface, rwUser: UserDBInterface): List<Report> {
        TODO("Not yet implemented")
    }

    fun seeMembers(data: SimpleOperator, rwCommunity: CommunityDBInterface, rwUser: UserDBInterface): List<String> {
        val user = rwUser.select(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.select(it) }

        if (user != null && community != null)
            return community.getMembers()

        return listOf()
    }

    fun seeTopics(data: SimpleOperator, rwTopic: TopicDBInterface, rwCommunity: CommunityDBInterface, rwUser: UserDBInterface): List<Topic> {
        val user = rwUser.select(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.select(it) }
        lateinit var topics: List<String>

        if (user != null && community != null) {
            topics = community.getTopics()

            val communityTopics = mutableListOf<Topic>()

            for (element in topics) {
                val topic = rwTopic.select(element, null)
                if (topic != null)
                    communityTopics.add(topic)
            }

            return communityTopics
        }

        return listOf()
    }

    fun seeGroups(data: SimpleOperator): List<Group> {
        TODO("Not yet implemented")
    }
}