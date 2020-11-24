package br.meetingplace.server.modules.topic.dao.dislike

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.requests.topics.operators.TopicSimpleOperator

object DislikeTopic{

    fun dislike(data: TopicSimpleOperator, rwUser: UserDBInterface, rwTopic: TopicDBInterface, rwCommunity: CommunityDBInterface): Status {
        return if (rwUser.check(data.login.email)) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    return when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> {
                            val topic = rwTopic.select(mainTopic = null, id = data.identifier.mainTopicID)
                            if (topic != null)
                                dislike(email = data.login.email, rwTopic = rwTopic, topic = topic)
                            else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                        } //MAIN
                        false -> {
                            val topic = rwTopic.select(mainTopic = data.identifier.mainTopicID, id = data.identifier.subTopicID)
                            if (topic != null)
                                dislike(topic, email = data.login.email, rwTopic)
                            else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                        } //SUB
                    }
                }
                false -> { //COMMUNITY
                    return when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> {//MAIN
                            val topic = rwTopic.select(mainTopic = null, id = data.identifier.mainTopicID)
                            if (rwCommunity.check(data.communityID) && topic != null && topic.getApproved())
                                dislike(email = data.login.email, rwTopic = rwTopic, topic = topic)
                            else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                        }
                        false -> {//SUB
                            val topic = rwTopic.select(mainTopic = data.identifier.mainTopicID, id = data.identifier.subTopicID)
                            if (rwCommunity.check(data.communityID) && topic != null)
                                dislike(email = data.login.email, rwTopic = rwTopic, topic = topic)
                            else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                        }
                    }
                }
            }
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

    private fun dislike(topic: Topic, email: String, rwTopic: TopicDBInterface):Status{
        lateinit var dislikes: List<String>
        lateinit var likes: List<String>

        return when (checkLikeDislike(topic, email)) {
            0 -> {
                likes = topic.getLikes()
                likes.remove(email)

                dislikes = topic.getDislikes()
                dislikes.add(email)

                topic.setLikes(likes)
                topic.setDislikes(dislikes)
                return rwTopic.insert(topic)
            } // like to dislike
            1 -> {
                dislikes = topic.getDislikes()
                dislikes.remove(email)

                topic.setDislikes(dislikes)
                return rwTopic.insert(topic)
            }
            2 -> {
                dislikes = topic.getDislikes()
                dislikes.add(email)

                topic.setDislikes(dislikes)
                return rwTopic.insert(topic)
            }
            else->Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
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