package br.meetingplace.server.services.topic.controller.dislike

import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.TopicLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.topics.TopicOperationsData
import br.meetingplace.server.services.topic.classes.Topic

class DislikeTopic private constructor() {
    companion object {
        private val Class = DislikeTopic()
        fun getClass() = Class
    }

    fun dislike(data: TopicOperationsData, rwUser: UserLSInterface, rwTopic: TopicLSInterface, rwCommunity: CommunityLSInterface) {
        val user = rwUser.load(data.login.email)

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


    private fun dislikeUserMainTopic(data: TopicOperationsData, rwTopic: TopicLSInterface) {
        val topic = rwTopic.load(data.identifier.mainTopicID, null)

        if (topic != null) {
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    println("1")
                    topic.likeToDislike(data.login.email)
                    rwTopic.store(topic)
                } // like to dislike
                1 -> {
                    println("2")
                    topic.removeDislike(data.login.email)
                    rwTopic.store(topic)
                }
                2 -> {
                    println("3")
                    topic.dislike(data.login.email)
                    rwTopic.store(topic)
                }
            }
        }
    }

    private fun dislikeUserSubTopic(data: TopicOperationsData, rwTopic: TopicLSInterface, ) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.load(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.load(data.identifier.mainTopicID, null)

        if (subTopic != null && mainTopic != null) {
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.likeToDislike(data.login.email)
                    rwTopic.store(subTopic)
                } // like to dislike
                1 -> {
                    subTopic.removeDislike(data.login.email)
                    rwTopic.store(subTopic)
                }
                2 -> {
                    subTopic.dislike(data.login.email)
                    rwTopic.store(subTopic)
                }
            }
        }
    }

    private fun dislikeCommunityMainTopic(data: TopicOperationsData, rwTopic: TopicLSInterface, rwCommunity: CommunityLSInterface) {
        val topic = rwTopic.load(data.identifier.mainTopicID, null)
        val community = data.communityID?.let { rwCommunity.load(it) }
        if (topic != null && community != null && community.checkTopicApproval(topic.getID())) {
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.likeToDislike(data.login.email)
                    rwTopic.store(topic)
                } // like to dislike
                1 -> {
                    topic.removeDislike(data.login.email)
                    rwTopic.store(topic)
                }
                2 -> {
                    topic.dislike(data.login.email)
                    rwTopic.store(topic)
                }
            }
        }
    }

    private fun dislikeCommunitySubtopic(data: TopicOperationsData, rwTopic: TopicLSInterface, rwCommunity: CommunityLSInterface, ) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.load(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.load(data.identifier.mainTopicID, null)

        val community = data.communityID?.let { rwCommunity.load(it) }

        if (subTopic != null && mainTopic != null && community != null && community.checkTopicApproval(mainTopic.getID())) {
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.likeToDislike(data.login.email)
                    rwTopic.store(subTopic)
                } // like to dislike
                1 -> {
                    subTopic.removeDislike(data.login.email)
                    rwTopic.store(subTopic)
                }
                2 -> {
                    subTopic.dislike(data.login.email)
                    rwTopic.store(subTopic)
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