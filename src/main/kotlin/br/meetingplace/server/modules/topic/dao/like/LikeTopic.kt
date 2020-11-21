package br.meetingplace.server.modules.topic.dao.like

import br.meetingplace.server.modules.global.dto.notification.NotificationData
import br.meetingplace.server.modules.global.dto.notification.types.NotificationMainType
import br.meetingplace.server.modules.global.dto.notification.types.NotificationSubType
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.topic.dto.Topic
import br.meetingplace.server.requests.topics.operators.TopicSimpleOperator

class LikeTopic private constructor() {
    companion object {
        private val Class = LikeTopic()
        fun getClass() = Class
    }

    fun like(data: TopicSimpleOperator, userDB: UserDBInterface, topicDB: TopicDBInterface, communityDB: CommunityDBInterface) {
        val user = userDB.select(data.login.email)

        if (user != null) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            likeUserMainTopic(data,userDB =  userDB,topicDB =  topicDB)
                        }
                        false -> { //SUB
                            likeUserSubTopic(data,userDB =  userDB,topicDB =  topicDB)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            likeCommunityMainTopic(data,userDB =  userDB,communityDB =  communityDB,topicDB =  topicDB)
                        }
                        false -> { //SUB
                            likeCommunitySubtopic(data,userDB =  userDB,communityDB =  communityDB,topicDB =  topicDB)
                        }
                    }
                }
            }//WHEN
        }//IF VERIFY USER
    }

    private fun likeUserMainTopic(data: TopicSimpleOperator, userDB: UserDBInterface, topicDB: TopicDBInterface) {
        val user = userDB.select(data.login.email)
        val topic = topicDB.select(data.identifier.mainTopicID, null)
        val creator = userDB.select(data.identifier.mainTopicOwner)
        lateinit var notification: NotificationData

        if (user != null && topic != null && creator != null) {
            notification = NotificationData(NotificationMainType.TOPIC, NotificationSubType.LIKE,user.getEmail())
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.removeLike(data.login.email)
                    topicDB.insert(topic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        userDB.insert(creator)
                    }
                    topic.dislikeToLike(data.login.email)
                    topicDB.insert(topic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        userDB.insert(creator)
                    }
                    topic.like(data.login.email)
                    topicDB.insert(topic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeUserSubTopic(data: TopicSimpleOperator, userDB: UserDBInterface, topicDB: TopicDBInterface) {
        val user = userDB.select(data.login.email)
        val subTopic = data.identifier.subTopicID?.let { topicDB.select(it, data.identifier.mainTopicID) }
        val creator = userDB.select(data.identifier.mainTopicOwner)
        lateinit var notification: NotificationData

        if (subTopic != null && user != null && creator != null) {
            notification = NotificationData(NotificationMainType.TOPIC, NotificationSubType.LIKE,user.getEmail())
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.removeLike(data.login.email)
                    topicDB.insert(subTopic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        userDB.insert(creator)
                    }
                    subTopic.dislikeToLike(data.login.email)
                    topicDB.insert(subTopic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        userDB.insert(creator)
                    }
                    subTopic.like(data.login.email)
                    topicDB.insert(subTopic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeCommunityMainTopic(data: TopicSimpleOperator, topicDB: TopicDBInterface, userDB: UserDBInterface, communityDB: CommunityDBInterface) {
        val user = userDB.select(data.login.email)
        val topic = topicDB.select(data.identifier.mainTopicID, null)
        val creator = userDB.select(data.identifier.mainTopicOwner)
        val community = data.communityID?.let { communityDB.select(it) }
        lateinit var notification: NotificationData

        if (topic != null && community != null && user != null && creator != null && community.checkTopicApproval(topic.getID())) {
            notification = NotificationData(NotificationMainType.TOPIC, NotificationSubType.LIKE,user.getEmail())
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.removeLike(data.login.email)
                    topicDB.insert(topic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        userDB.insert(creator)
                    }
                    topic.dislikeToLike(data.login.email)
                    topicDB.insert(topic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        userDB.insert(creator)
                    }
                    topic.like(data.login.email)
                    topicDB.insert(topic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeCommunitySubtopic(data: TopicSimpleOperator, userDB: UserDBInterface, communityDB: CommunityDBInterface, topicDB: TopicDBInterface) {
        val user = userDB.select(data.login.email)
        val subTopic = data.identifier.subTopicID?.let { topicDB.select(it, data.identifier.mainTopicID) }
        val creator = userDB.select(data.identifier.mainTopicOwner)
        val community = data.communityID?.let { communityDB.select(it) }
        lateinit var notification: NotificationData

        if (subTopic != null && community != null && user != null && creator != null && community.checkTopicApproval(subTopic.getID())) {
            notification = NotificationData(NotificationMainType.TOPIC, NotificationSubType.LIKE,user.getEmail())
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.removeLike(data.login.email)
                    topicDB.insert(subTopic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        userDB.insert(creator)
                    }
                    subTopic.dislikeToLike(data.login.email)
                    topicDB.insert(subTopic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        userDB.insert(creator)
                    }
                    subTopic.like(data.login.email)
                    topicDB.insert(subTopic)
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