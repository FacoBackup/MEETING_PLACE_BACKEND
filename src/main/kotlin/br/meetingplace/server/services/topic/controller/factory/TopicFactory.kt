package br.meetingplace.server.services.topic.controller.factory

import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.TopicLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.topics.TopicData
import br.meetingplace.server.services.members.data.MemberType
import br.meetingplace.server.data.classes.owner.OwnerType
import br.meetingplace.server.data.classes.owner.topic.TopicOwnerData
import br.meetingplace.server.services.topic.classes.SimplifiedTopic
import br.meetingplace.server.services.topic.classes.Topic
import java.util.*

class TopicFactory private constructor() {
    companion object {
        private val Class = TopicFactory()
        fun getClass() = Class
    }

    fun create(data: TopicData, rwTopic: TopicLSInterface, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface) {
        val user = rwUser.load(data.login.email)

        if (user != null) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier == null) {
                        true -> { //MAIN
                            createUserMainTopic(data, rwUser, rwTopic)
                        }
                        false -> { //SUB
                            createUserSubTopic(data, rwUser, rwTopic)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier == null) {
                        true -> { //MAIN
                            createCommunityMainTopic(data, rwUser, rwCommunity, rwTopic)
                        }
                        false -> { //SUB
                            createCommunitySubTopic(data, rwUser, rwCommunity, rwTopic)
                        }
                    }
                }
            }//WHEN
        }//IF VERIFY USER
    }

    private fun createCommunityMainTopic(data: TopicData, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface, rwTopic: TopicLSInterface) {
        val user = rwUser.load(data.login.email)
        val community = data.communityID?.let { rwCommunity.load(it) }

        if (data.identifier != null && community != null && user != null) {
            val topic = Topic(TopicOwnerData(community.getID(), data.login.email, OwnerType.COMMUNITY), user.getEmail(), UUID.randomUUID().toString(), null)

            when (community.getMemberRole(data.login.email)) {
                MemberType.CREATOR -> {
                    community.updateTopicInValidation(SimplifiedTopic(topic.getID(), topic.getOwner()), true)
                }
                MemberType.MODERATOR -> {
                    community.updateTopicInValidation(SimplifiedTopic(topic.getID(), topic.getOwner()), true)
                }
                MemberType.NORMAL -> {
                    community.updateTopicInValidation(SimplifiedTopic(topic.getID(), topic.getOwner()), null)
                }
            }
            rwTopic.store(topic)
            rwUser.store(user)
            rwCommunity.store(community)
        }
    }

    private fun createCommunitySubTopic(data: TopicData, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface, rwTopic: TopicLSInterface) {
        val user = rwUser.load(data.login.email)
        val community = data.communityID?.let { rwCommunity.load(it) }

        if (data.identifier != null && community != null && user != null) {
            val mainTopic = rwTopic.load(data.identifier.mainTopicID, null)
            if (mainTopic != null && community.checkTopicApproval(mainTopic.getID())) {
                val subtopic = Topic(mainTopic.getOwner(), user.getEmail(), UUID.randomUUID().toString(), data.identifier.mainTopicID)
                mainTopic.addSubTopic(subtopic.getID())
                subtopic.addContent(data.header, data.body, user.getUserName())
                rwTopic.store(mainTopic)
                rwTopic.store(subtopic)
                rwUser.store(user)
            }
        }
    }

    private fun createUserMainTopic(data: TopicData, rwUser: UserLSInterface, rwTopic: TopicLSInterface) {
        val user = rwUser.load(data.login.email)

        if(user != null){
            val topic = Topic(TopicOwnerData(user.getEmail(), user.getEmail(), OwnerType.USER), user.getEmail(), UUID.randomUUID().toString(), null)

            topic.addContent(data.header, data.body, user.getUserName())
            rwTopic.store(topic)
            user.updateMyTopics(topic.getID(), true)
            rwUser.store(user)
        }
    }

    private fun createUserSubTopic(data: TopicData, rwUser: UserLSInterface, rwTopic: TopicLSInterface) {
        val user = rwUser.load(data.login.email)

        if (data.identifier != null && user != null) {
            val mainTopic = rwTopic.load(data.identifier.mainTopicID, null)
            if (mainTopic != null) {
                val subtopic = Topic(mainTopic.getOwner(), user.getEmail(), UUID.randomUUID().toString(), data.identifier.mainTopicID)
                mainTopic.addSubTopic(subtopic.getID())
                subtopic.addContent(data.header, data.body, user.getUserName())
                rwTopic.store(mainTopic)
                rwTopic.store(subtopic)
                rwUser.store(user)
            }
        }
    }

}