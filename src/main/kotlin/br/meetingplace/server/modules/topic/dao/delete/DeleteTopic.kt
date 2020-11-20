package br.meetingplace.server.modules.topic.dao.delete

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.modules.topic.dto.SimplifiedTopic
import br.meetingplace.server.modules.topic.dto.Topic
import br.meetingplace.server.requests.topics.operators.TopicSimpleOperator

class DeleteTopic private constructor() {
    companion object {
        private val Class = DeleteTopic()
        fun getClass() = Class
    }

    fun delete(data: TopicSimpleOperator, rwUser: UserDBInterface, rwTopic: TopicDBInterface, rwCommunity: CommunityDBInterface) {
        val user = rwUser.select(data.login.email)

        if (user != null) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> deleteUserMainTopic(data,rwTopic, rwUser) //MAIN
                        false -> deleteUserSubTopic(data,rwTopic) //SUB
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> deleteCommunityMainTopic(data, rwCommunity, rwTopic) //MAIN
                        false -> deleteCommunitySubTopic(data,rwCommunity, rwTopic) //SUB
                    }
                }
            }
        }
    }

    private fun deleteUserMainTopic(data: TopicSimpleOperator, rwTopic: TopicDBInterface, rwUser: UserDBInterface) {
        val user = rwUser.select(data.login.email)
        val topic = rwTopic.select(data.identifier.mainTopicID, null)

        if (topic != null && user != null && topic.getCreator() == data.login.email) {
            user.updateMyTopics(topic.getID(), false)
            deleteAllSubTopics(topic, rwTopic)
            rwTopic.delete(topic)
            rwUser.insert(user)
        }
    }

    private fun deleteUserSubTopic(data: TopicSimpleOperator, rwTopic: TopicDBInterface) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.select(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.select(data.identifier.mainTopicID, null)
        if (subTopic != null && mainTopic != null && subTopic.getCreator() == data.login.email) {
            subTopic.removeSubTopic(data.identifier.subTopicID)
            mainTopic.removeSubTopic(subTopic.getID())
            rwTopic.insert(mainTopic)
            rwTopic.delete(subTopic)
        }
    }

    private fun deleteCommunityMainTopic(data: TopicSimpleOperator, rwCommunity: CommunityDBInterface, rwTopic: TopicDBInterface) {
        val mainTopic = rwTopic.select(data.identifier.mainTopicID, null)
        val community = data.communityID?.let { rwCommunity.select(it) }

        if (mainTopic!= null && community != null && community.checkTopicApproval(mainTopic.getID()) &&
                (mainTopic.getCreator() == data.login.email || community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {

            community.removeApprovedTopic(SimplifiedTopic(mainTopic.getID(), mainTopic.getOwner()))
            deleteAllSubTopics(mainTopic,rwTopic)
            rwTopic.delete(mainTopic)
        }
    }

    private fun deleteCommunitySubTopic(data: TopicSimpleOperator, rwCommunity: CommunityDBInterface, rwTopic: TopicDBInterface) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.select(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.select(data.identifier.mainTopicID, null)
        val community = data.communityID?.let { rwCommunity.select(it) }

        if (subTopic != null && mainTopic != null && community != null && community.checkTopicApproval(mainTopic.getID()) &&
                (subTopic.getCreator() == data.login.email || community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {

            subTopic.removeSubTopic(data.identifier.subTopicID)
            mainTopic.removeSubTopic(subTopic.getID())
            rwTopic.insert(mainTopic)
            rwTopic.delete(subTopic)
        }
    }

    private fun deleteAllSubTopics(mainTopic: Topic, rwTopic: TopicDBInterface) {
        val subTopics = mainTopic.getSubTopics()

        for (i in subTopics.indices) {
            val subtopic = rwTopic.select(subTopics[i], mainTopic.getID())
            if(subtopic != null)
                rwTopic.delete(subtopic)
        }
    }
}