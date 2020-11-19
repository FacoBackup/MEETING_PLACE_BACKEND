package br.meetingplace.server.services.topic.controller.delete

import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.TopicLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.topics.TopicOperationsData
import br.meetingplace.server.services.members.data.MemberType
import br.meetingplace.server.services.topic.classes.SimplifiedTopic
import br.meetingplace.server.services.topic.classes.Topic

class DeleteTopic private constructor() {
    companion object {
        private val Class = DeleteTopic()
        fun getClass() = Class
    }

    fun delete(data: TopicOperationsData, rwUser: UserLSInterface, rwTopic: TopicLSInterface, rwCommunity: CommunityLSInterface) {
        val user = rwUser.load(data.login.email)

        if (user != null) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            deleteUserMainTopic(data,rwTopic, rwUser)
                        }
                        false -> { //SUB
                            deleteUserSubTopic(data,rwTopic)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            deleteCommunityMainTopic(data, rwCommunity, rwTopic)
                        }
                        false -> { //SUB
                            deleteCommunitySubTopic(data,rwCommunity, rwTopic)
                        }
                    }
                }
            }//WHEN
        }//IF VERIFY USER
    }//DELETE

    private fun deleteUserMainTopic(data: TopicOperationsData, rwTopic: TopicLSInterface, rwUser: UserLSInterface) {
        val user = rwUser.load(data.login.email)
        val topic = rwTopic.load(data.identifier.mainTopicID, null)

        if (topic != null && user != null && topic.getCreator() == data.login.email) {
            user.updateMyTopics(topic.getID(), false)
            deleteAllSubTopics(topic, rwTopic)
            rwTopic.delete(topic)
            rwUser.store(user)
        }
    }

    private fun deleteUserSubTopic(data: TopicOperationsData, rwTopic: TopicLSInterface) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.load(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.load(data.identifier.mainTopicID, null)
        if (subTopic != null && mainTopic != null && subTopic.getCreator() == data.login.email) {
            subTopic.removeSubTopic(data.identifier.subTopicID)
            mainTopic.removeSubTopic(subTopic.getID())
            rwTopic.store(mainTopic)
            rwTopic.delete(subTopic)
        }
    }

    private fun deleteCommunityMainTopic(data: TopicOperationsData, rwCommunity: CommunityLSInterface, rwTopic: TopicLSInterface) {
        val mainTopic = rwTopic.load(data.identifier.mainTopicID, null)
        val community = data.communityID?.let { rwCommunity.load(it) }

        if (mainTopic!= null && community != null && community.checkTopicApproval(mainTopic.getID()) &&
                (mainTopic.getCreator() == data.login.email || community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {

            community.removeApprovedTopic(SimplifiedTopic(mainTopic.getID(), mainTopic.getOwner()))
            deleteAllSubTopics(mainTopic,rwTopic)
            rwTopic.delete(mainTopic)
        }
    }

    private fun deleteCommunitySubTopic(data: TopicOperationsData, rwCommunity: CommunityLSInterface, rwTopic: TopicLSInterface) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.load(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.load(data.identifier.mainTopicID, null)
        val community = data.communityID?.let { rwCommunity.load(it) }

        if (subTopic != null && mainTopic != null && community != null && community.checkTopicApproval(mainTopic.getID()) &&
                (subTopic.getCreator() == data.login.email || community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {

            subTopic.removeSubTopic(data.identifier.subTopicID)
            mainTopic.removeSubTopic(subTopic.getID())
            rwTopic.store(mainTopic)
            rwTopic.delete(subTopic)
        }
    }

    private fun deleteAllSubTopics(mainTopic: Topic, rwTopic: TopicLSInterface) {
        val subTopics = mainTopic.getSubTopics()

        for (i in subTopics.indices) {
            val subtopic = rwTopic.load(subTopics[i], mainTopic.getID())
            if(subtopic != null)
                rwTopic.delete(subtopic)
        }
    }
}