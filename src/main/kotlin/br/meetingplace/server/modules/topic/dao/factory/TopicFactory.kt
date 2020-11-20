package br.meetingplace.server.modules.topic.dao.factory

import br.meetingplace.server.dto.owner.OwnerType
import br.meetingplace.server.db.interfaces.CommunityDBInterface
import br.meetingplace.server.db.interfaces.TopicDBInterface
import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.modules.topic.dto.SimplifiedTopic
import br.meetingplace.server.modules.topic.dto.Topic
import br.meetingplace.server.modules.topic.dto.TopicOwnerData
import br.meetingplace.server.routers.topics.requests.TopicData
import java.util.*

class TopicFactory private constructor() {
    companion object {
        private val Class = TopicFactory()
        fun getClass() = Class
    }

    fun create(data: TopicData, rwTopic: TopicDBInterface, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface) {
        val user = rwUser.select(data.login.email)

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

    private fun createCommunityMainTopic(data: TopicData, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.login.email)
        val community = data.communityID?.let { rwCommunity.select(it) }

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
            rwTopic.insert(topic)
            rwUser.insert(user)
            rwCommunity.insert(community)
        }
    }

    private fun createCommunitySubTopic(data: TopicData, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.login.email)
        val community = data.communityID?.let { rwCommunity.select(it) }

        if (data.identifier != null && community != null && user != null) {
            val mainTopic = rwTopic.select(data.identifier.mainTopicID, null)
            if (mainTopic != null && community.checkTopicApproval(mainTopic.getID())) {
                val subtopic = Topic(mainTopic.getOwner(), user.getEmail(), UUID.randomUUID().toString(), data.identifier.mainTopicID)
                mainTopic.addSubTopic(subtopic.getID())
                subtopic.addContent(data.header, data.body, user.getUserName())
                rwTopic.insert(mainTopic)
                rwTopic.insert(subtopic)
                rwUser.insert(user)
            }
        }
    }

    private fun createUserMainTopic(data: TopicData, rwUser: UserDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.login.email)

        if(user != null){
            val topic = Topic(TopicOwnerData(user.getEmail(), user.getEmail(), OwnerType.USER), user.getEmail(), UUID.randomUUID().toString(), null)

            topic.addContent(data.header, data.body, user.getUserName())
            rwTopic.insert(topic)
            user.updateMyTopics(topic.getID(), true)
            rwUser.insert(user)
        }
    }

    private fun createUserSubTopic(data: TopicData, rwUser: UserDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.login.email)

        if (data.identifier != null && user != null) {
            val mainTopic = rwTopic.select(data.identifier.mainTopicID, null)
            if (mainTopic != null) {
                val subtopic = Topic(mainTopic.getOwner(), user.getEmail(), UUID.randomUUID().toString(), data.identifier.mainTopicID)
                mainTopic.addSubTopic(subtopic.getID())
                subtopic.addContent(data.header, data.body, user.getUserName())
                rwTopic.insert(mainTopic)
                rwTopic.insert(subtopic)
                rwUser.insert(user)
            }
        }
    }

}