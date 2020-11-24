package br.meetingplace.server.modules.topic.dao.delete

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.global.methods.member.getMemberRole
import br.meetingplace.server.modules.members.classes.MemberType
import br.meetingplace.server.modules.topic.classes.Topic
import br.meetingplace.server.requests.topics.operators.TopicSimpleOperator

object DeleteTopic{

    fun delete(data: TopicSimpleOperator, rwUser: UserDBInterface, rwTopic: TopicDBInterface, rwCommunity: CommunityDBInterface):Status{

        return if (rwUser.check(data.login.email)) {
            return when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> deleteUserMainTopic(data, rwTopic, rwUser) //MAIN
                        false -> deleteUserSubTopic(data, rwTopic) //SUB
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> deleteCommunityMainTopic(data, rwCommunity, rwTopic) //MAIN
                        false -> deleteCommunitySubTopic(data, rwCommunity, rwTopic) //SUB
                    }
                }
            }
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

    private fun deleteUserMainTopic(data: TopicSimpleOperator, rwTopic: TopicDBInterface, rwUser: UserDBInterface):Status{
        val user = rwUser.select(data.login.email)
        val topic = rwTopic.select(data.identifier.mainTopicID, null)
        lateinit var userTopics: List<String>

        return if (topic != null && user != null && topic.getCreator() == data.login.email) {
            userTopics = user.getTopics()
            userTopics.remove(topic.getID())
            user.setTopics(userTopics)

            deleteAllSubTopics(topic, rwTopic)
            rwUser.insert(user)

            return rwTopic.delete(topic)
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

    private fun deleteUserSubTopic(data: TopicSimpleOperator, rwTopic: TopicDBInterface):Status{
        val subTopic = data.identifier.subTopicID?.let { rwTopic.select(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.select(data.identifier.mainTopicID, null)
        lateinit var subtopics: List<String>

        return if (subTopic != null && mainTopic != null && subTopic.getCreator() == data.login.email) {
            subtopics = mainTopic.getComments()
            subtopics.remove(subTopic.getID())
            mainTopic.setComments(subtopics)
            rwTopic.insert(mainTopic)
            return rwTopic.delete(subTopic)
        }else  Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

    private fun deleteCommunityMainTopic(data: TopicSimpleOperator, rwCommunity: CommunityDBInterface, rwTopic: TopicDBInterface):Status {
        val mainTopic = rwTopic.select(data.identifier.mainTopicID, null)
        val community = data.communityID?.let { rwCommunity.select(it) }
        lateinit var approved: List<String>

        return if (mainTopic != null && community != null && mainTopic.getID() in community.getTopics() &&
                (mainTopic.getCreator() == data.login.email || getMemberRole(community.getMembers(),data.login.email) == MemberType.MODERATOR)) {

            approved = community.getTopics()
            approved.remove(mainTopic.getID())
            community.setTopics(approved)
            deleteAllSubTopics(mainTopic, rwTopic)
            return rwTopic.delete(mainTopic)
        }else  Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

    private fun deleteCommunitySubTopic(data: TopicSimpleOperator, rwCommunity: CommunityDBInterface, rwTopic: TopicDBInterface):Status {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.select(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.select(data.identifier.mainTopicID, null)
        val community = data.communityID?.let { rwCommunity.select(it) }
        lateinit var subtopics: List<String>

        return if (subTopic != null && mainTopic != null && community != null && mainTopic.getID() in community.getTopics() &&
                (subTopic.getCreator() == data.login.email || getMemberRole(community.getMembers(),data.login.email)  == MemberType.MODERATOR)) {

            subtopics = mainTopic.getComments()
            subtopics.remove(subTopic.getID())
            mainTopic.setComments(subtopics)

            rwTopic.insert(mainTopic)
            return rwTopic.delete(subTopic)
        }else  Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

    private fun deleteAllSubTopics(mainTopic: Topic, rwTopic: TopicDBInterface) {
        val subTopics = mainTopic.getComments()

        for (i in subTopics.indices) {
            val subtopic = rwTopic.select(subTopics[i], mainTopic.getID())

            if (subtopic != null) {
                deleteAllSubTopics(subtopic, rwTopic)
                rwTopic.delete(subtopic)
            }
        }
    }
}