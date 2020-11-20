package br.meetingplace.server.modules.topic.dao.dislike

import br.meetingplace.server.db.interfaces.CommunityDBInterface
import br.meetingplace.server.db.interfaces.TopicDBInterface
import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.modules.topic.dto.Topic
import br.meetingplace.server.routers.topics.requests.TopicOperationsData

class DislikeTopic private constructor() {
    companion object {
        private val Class = DislikeTopic()
        fun getClass() = Class
    }

    fun dislike(data: TopicOperationsData, rwUser: UserDBInterface, rwTopic: TopicDBInterface, rwCommunity: CommunityDBInterface) {
        val user = rwUser.select(data.login.email)

        if (user != null) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            dislikeUserMainTopic(data, rwTopic)
                        }
                        false -> { //SUB
                            dislikeUserSubTopic(data, rwTopic)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            dislikeCommunityMainTopic(data, rwTopic, rwCommunity)
                        }
                        false -> { //SUB
                            dislikeCommunitySubtopic(data, rwTopic, rwCommunity)
                        }
                    }
                }
            }
        }
    }


    private fun dislikeUserMainTopic(data: TopicOperationsData, rwTopic: TopicDBInterface) {
        val topic = rwTopic.select(data.identifier.mainTopicID, null)

        if (topic != null) {
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    println("1")
                    topic.likeToDislike(data.login.email)
                    rwTopic.insert(topic)
                } // like to dislike
                1 -> {
                    println("2")
                    topic.removeDislike(data.login.email)
                    rwTopic.insert(topic)
                }
                2 -> {
                    println("3")
                    topic.dislike(data.login.email)
                    rwTopic.insert(topic)
                }
            }
        }
    }

    private fun dislikeUserSubTopic(data: TopicOperationsData, rwTopic: TopicDBInterface, ) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.select(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.select(data.identifier.mainTopicID, null)

        if (subTopic != null && mainTopic != null) {
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.likeToDislike(data.login.email)
                    rwTopic.insert(subTopic)
                } // like to dislike
                1 -> {
                    subTopic.removeDislike(data.login.email)
                    rwTopic.insert(subTopic)
                }
                2 -> {
                    subTopic.dislike(data.login.email)
                    rwTopic.insert(subTopic)
                }
            }
        }
    }

    private fun dislikeCommunityMainTopic(data: TopicOperationsData, rwTopic: TopicDBInterface, rwCommunity: CommunityDBInterface) {
        val topic = rwTopic.select(data.identifier.mainTopicID, null)
        val community = data.communityID?.let { rwCommunity.select(it) }
        if (topic != null && community != null && community.checkTopicApproval(topic.getID())) {
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.likeToDislike(data.login.email)
                    rwTopic.insert(topic)
                } // like to dislike
                1 -> {
                    topic.removeDislike(data.login.email)
                    rwTopic.insert(topic)
                }
                2 -> {
                    topic.dislike(data.login.email)
                    rwTopic.insert(topic)
                }
            }
        }
    }

    private fun dislikeCommunitySubtopic(data: TopicOperationsData, rwTopic: TopicDBInterface, rwCommunity: CommunityDBInterface, ) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.select(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.select(data.identifier.mainTopicID, null)

        val community = data.communityID?.let { rwCommunity.select(it) }

        if (subTopic != null && mainTopic != null && community != null && community.checkTopicApproval(mainTopic.getID())) {
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.likeToDislike(data.login.email)
                    rwTopic.insert(subTopic)
                } // like to dislike
                1 -> {
                    subTopic.removeDislike(data.login.email)
                    rwTopic.insert(subTopic)
                }
                2 -> {
                    subTopic.dislike(data.login.email)
                    rwTopic.insert(subTopic)
                }
            }
        }
    }

    private fun checkLikeDislike(topic: Topic, user: String): Int {
        return when (user) {
            in topic.getLikes() // 0 ALREADY LIKED
            -> 0
            in topic.getDislikes() // 1 ALREADY DISLIKED
            -> 1
            else -> 2 // 2 hasn't DISLIKED or liked yet
        }
    }
}