package br.meetingplace.server.modules.topic.dao.like

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
        if (userDB.check(data.login.email)) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> {
                            val topic = topicDB.select(mainTopic = null, id = data.identifier.mainTopicID)
                            if (topic != null)
                                like(email = data.login.email, rwTopic = topicDB, topic = topic)
                        } //MAIN
                        false -> {
                            val topic = topicDB.select(mainTopic = data.identifier.mainTopicID, id = data.identifier.subTopicID)
                            if (topic != null)
                                like(topic, email = data.login.email, topicDB)
                        } //SUB
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> {//MAIN
                            val topic = topicDB.select(mainTopic = null, id = data.identifier.mainTopicID)
                            if (communityDB.check(data.communityID) && topic != null && topic.getApproved())
                                like(email = data.login.email, rwTopic = topicDB, topic = topic)
                        }
                        false -> {//SUB
                            val topic = topicDB.select(mainTopic = data.identifier.mainTopicID, id = data.identifier.subTopicID)
                            if (communityDB.check(data.communityID) && topic != null)
                                like(email = data.login.email, rwTopic = topicDB, topic = topic)
                        }
                    }
                }
            }
        }
    }

    private fun like(topic: Topic, email: String, rwTopic: TopicDBInterface) {
        lateinit var dislikes: List<String>
        lateinit var likes: List<String>

        when (checkLikeDislike(topic, email)) {
            0 -> {
                likes = topic.getLikes()
                likes.remove(email)

                topic.setLikes(dislikes)
                rwTopic.insert(topic)
            }
            1 -> {
                likes = topic.getLikes()
                likes.add(email)

                dislikes = topic.getDislikes()
                dislikes.remove(email)

                topic.setLikes(likes)
                topic.setDislikes(dislikes)
                rwTopic.insert(topic)
            } //dislikeToLike
            2 -> {
                likes = topic.getLikes()
                likes.add(email)

                topic.setLikes(dislikes)
                rwTopic.insert(topic)
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