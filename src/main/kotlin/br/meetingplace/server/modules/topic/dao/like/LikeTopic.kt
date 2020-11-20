package br.meetingplace.server.modules.topic.dao.like

import br.meetingplace.server.modules.notification.dto.NotificationData
import br.meetingplace.server.modules.notification.dto.types.NotificationMainType
import br.meetingplace.server.modules.notification.dto.types.NotificationSubType
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.topic.dto.Topic
import br.meetingplace.server.requests.topics.TopicOperationsData

class LikeTopic private constructor() {
    companion object {
        private val Class = LikeTopic()
        fun getClass() = Class
    }

    fun like(data: TopicOperationsData, rwUser: UserDBInterface, rwTopic: TopicDBInterface, rwCommunity: CommunityDBInterface) {
        val user = rwUser.select(data.login.email)

        if (user != null) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            likeUserMainTopic(data,rwUser =  rwUser,rwTopic =  rwTopic)
                        }
                        false -> { //SUB
                            likeUserSubTopic(data,rwUser =  rwUser,rwTopic =  rwTopic)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            likeCommunityMainTopic(data,rwUser =  rwUser,rwCommunity =  rwCommunity,rwTopic =  rwTopic)
                        }
                        false -> { //SUB
                            likeCommunitySubtopic(data,rwUser =  rwUser,rwCommunity =  rwCommunity,rwTopic =  rwTopic)
                        }
                    }
                }
            }//WHEN
        }//IF VERIFY USER
    }

    private fun likeUserMainTopic(data: TopicOperationsData, rwUser: UserDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.login.email)
        val topic = rwTopic.select(data.identifier.mainTopicID, null)
        val creator = rwUser.select(data.identifier.mainTopicOwner)
        lateinit var notification: NotificationData

        if (user != null && topic != null && creator != null) {
            notification = NotificationData(NotificationMainType.TOPIC, NotificationSubType.LIKE)
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.removeLike(data.login.email)
                    rwTopic.insert(topic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.insert(creator)
                    }
                    topic.dislikeToLike(data.login.email)
                    rwTopic.insert(topic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.insert(creator)
                    }
                    topic.like(data.login.email)
                    rwTopic.insert(topic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeUserSubTopic(data: TopicOperationsData, rwUser: UserDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.login.email)
        val subTopic = data.identifier.subTopicID?.let { rwTopic.select(it, data.identifier.mainTopicID) }
        val creator = rwUser.select(data.identifier.mainTopicOwner)
        lateinit var notification: NotificationData

        if (subTopic != null && user != null && creator != null) {
            notification = NotificationData(NotificationMainType.TOPIC, NotificationSubType.LIKE)
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.removeLike(data.login.email)
                    rwTopic.insert(subTopic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.insert(creator)
                    }
                    subTopic.dislikeToLike(data.login.email)
                    rwTopic.insert(subTopic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.insert(creator)
                    }
                    subTopic.like(data.login.email)
                    rwTopic.insert(subTopic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeCommunityMainTopic(data: TopicOperationsData, rwTopic: TopicDBInterface, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface) {
        val user = rwUser.select(data.login.email)
        val topic = rwTopic.select(data.identifier.mainTopicID, null)
        val creator = rwUser.select(data.identifier.mainTopicOwner)
        val community = data.communityID?.let { rwCommunity.select(it) }
        lateinit var notification: NotificationData

        if (topic != null && community != null && user != null && creator != null && community.checkTopicApproval(topic.getID())) {
            notification = NotificationData(NotificationMainType.TOPIC, NotificationSubType.LIKE)
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.removeLike(data.login.email)
                    rwTopic.insert(topic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.insert(creator)
                    }
                    topic.dislikeToLike(data.login.email)
                    rwTopic.insert(topic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.insert(creator)
                    }
                    topic.like(data.login.email)
                    rwTopic.insert(topic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeCommunitySubtopic(data: TopicOperationsData, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.login.email)
        val subTopic = data.identifier.subTopicID?.let { rwTopic.select(it, data.identifier.mainTopicID) }
        val creator = rwUser.select(data.identifier.mainTopicOwner)
        val community = data.communityID?.let { rwCommunity.select(it) }
        lateinit var notification: NotificationData

        if (subTopic != null && community != null && user != null && creator != null && community.checkTopicApproval(subTopic.getID())) {
            notification = NotificationData(NotificationMainType.TOPIC, NotificationSubType.LIKE)
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.removeLike(data.login.email)
                    rwTopic.insert(subTopic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.insert(creator)
                    }
                    subTopic.dislikeToLike(data.login.email)
                    rwTopic.insert(subTopic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.insert(creator)
                    }
                    subTopic.like(data.login.email)
                    rwTopic.insert(subTopic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun checkLikeDislike(topic: Topic, user: String): Int {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD
        return when (user) {
            in topic.getLikes() // 0 ALREADY LIKED
            -> 0
            in topic.getDislikes() // 1 ALREADY DISLIKED
            -> 1
            else -> 2 // 2 hasn't DISLIKED or liked yet
        }
    }
}