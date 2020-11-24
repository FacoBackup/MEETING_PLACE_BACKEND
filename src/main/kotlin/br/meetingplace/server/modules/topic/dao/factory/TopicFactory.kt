package br.meetingplace.server.modules.topic.dao.factory

import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.global.methods.member.getMemberRole
import br.meetingplace.server.modules.members.classes.MemberType
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.requests.topics.data.TopicData
import java.util.*

object TopicFactory {

    fun create(data: TopicData, topicDB: TopicDBInterface, userDB: UserDBInterface, communityDB: CommunityDBInterface): Status {


        when (data.communityID.isNullOrBlank()) {
            true -> { //USER
                return when (data.identifier == null) {
                    true -> createUserMainTopic(data, userDB, topicDB) //MAIN
                    false -> {
                        val mainTopic = topicDB.select(data.identifier.mainTopicID, null)
                        return if (userDB.check(data.login.email) && topicDB.check(data.identifier.mainTopicID) && mainTopic != null)
                            createUserSubTopic(data, userDB = userDB, topicDB = topicDB, mainTopic = mainTopic)
                        else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                    }//SUB
                }
            }
            false -> { //COMMUNITY
                return when (data.identifier == null) {
                    true -> {
                        if (communityDB.check(data.communityID))//MAIN
                            createCommunityMainTopic(data, userDB, communityDB, topicDB)
                        else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                    }
                    false -> {
                        val mainTopic = topicDB.select(data.identifier.mainTopicID, null)
                        if (communityDB.check(data.communityID) && topicDB.check(data.identifier.mainTopicID) && mainTopic != null) //SUB
                            createCommunitySubTopic(data, communityDB = communityDB, topicDB = topicDB, userDB = userDB, mainTopic = mainTopic)
                        else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                    }

                }
            }
        }//WHEN
    }

    private fun createCommunityMainTopic(data: TopicData, userDB: UserDBInterface, communityDB: CommunityDBInterface, topicDB: TopicDBInterface) :Status{
        val user = userDB.select(data.login.email)
        val community = data.communityID?.let { communityDB.select(it) }
        lateinit var topic: Topic
        lateinit var topics: List<String>

        return if (data.identifier != null && community != null && user != null) {
            topic = Topic(approved = getMemberRole(community.getMembers(),data.login.email) == MemberType.MODERATOR, id = UUID.randomUUID().toString(), creator = user.getEmail(), footer = user.getUserName(), mainTopic = null)
            topics = user.getTopics()
            topics.add(topic.getID())
            user.setTopics(topics)

            topics = community.getTopics()
            topics.add(topic.getID())
            community.setTopics(topics)

            userDB.insert(user)
            communityDB.insert(community)
            return topicDB.insert(topic)
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

    private fun createCommunitySubTopic(data: TopicData, mainTopic: Topic, userDB: UserDBInterface, communityDB: CommunityDBInterface, topicDB: TopicDBInterface): Status {
        val user = userDB.select(data.login.email)
        val community = data.communityID?.let { communityDB.select(it) }
        lateinit var topic: Topic
        lateinit var comments: List<String>

        return if (data.identifier != null && community != null && user != null && mainTopic.getApproved()) {
            topic = Topic(approved = true, id = UUID.randomUUID().toString(), creator = user.getEmail(), footer = user.getUserName(), mainTopic = mainTopic.getMainTopic())

            comments = mainTopic.getComments()
            comments.add(topic.getID())
            mainTopic.setComments(comments)

            topicDB.insert(mainTopic)
            userDB.insert(user)
            return topicDB.insert(topic)
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

    private fun createUserMainTopic(data: TopicData, userDB: UserDBInterface, topicDB: TopicDBInterface):Status {
        val user = userDB.select(data.login.email)
        lateinit var topics: List<String>

        return if (user != null) {
            val topic = Topic(approved = true, id = UUID.randomUUID().toString(), creator = user.getEmail(), footer = user.getUserName(), mainTopic = null)
            topics = user.getTopics()
            topics.add(topic.getID())
            user.setTopics(topics)

            userDB.insert(user)
            return topicDB.insert(topic)
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

    private fun createUserSubTopic(data: TopicData, mainTopic: Topic, userDB: UserDBInterface, topicDB: TopicDBInterface):Status {
        val user = userDB.select(data.login.email)
        lateinit var topic: Topic
        lateinit var comments: List<String>

        return if (data.identifier != null && user != null) {
            topic = Topic(approved = true, id = UUID.randomUUID().toString(), creator = user.getEmail(), footer = user.getUserName(), mainTopic = mainTopic.getID())

            comments = mainTopic.getComments()
            comments.add(topic.getID())
            mainTopic.setComments(comments)

            topicDB.insert(mainTopic)

            userDB.insert(user)
            return topicDB.insert(topic)
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

}