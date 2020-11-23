package br.meetingplace.server.modules.topic.dao.factory

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.topic.dto.Topic
import br.meetingplace.server.requests.topics.data.TopicData
import java.util.*

class TopicFactory private constructor() {
    companion object {
        private val Class = TopicFactory()
        fun getClass() = Class
    }

    fun create(data: TopicData, topicDB: TopicDBInterface, userDB: UserDBInterface, communityDB: CommunityDBInterface) {


        when (data.communityID.isNullOrBlank()) {
            true -> { //USER
                when (data.identifier == null) {
                    true -> createUserMainTopic(data, userDB, topicDB) //MAIN
                    false -> {
                        val mainTopic = topicDB.select(data.identifier.mainTopicID, null)
                        if (userDB.check(data.login.email) && topicDB.check(data.identifier.mainTopicID) && mainTopic != null)
                            createUserSubTopic(data, userDB = userDB, topicDB = topicDB, mainTopic = mainTopic)
                    }//SUB
                }
            }
            false -> { //COMMUNITY
                when (data.identifier == null) {
                    true -> if (communityDB.check(data.communityID))//MAIN
                        createCommunityMainTopic(data, userDB, communityDB, topicDB)

                    false -> {
                        val mainTopic = topicDB.select(data.identifier.mainTopicID, null)
                        if (communityDB.check(data.communityID) && topicDB.check(data.identifier.mainTopicID) && mainTopic != null) //SUB
                            createCommunitySubTopic(data, communityDB = communityDB, topicDB = topicDB, userDB = userDB, mainTopic = mainTopic)
                    }

                }
            }
        }//WHEN
    }

    private fun createCommunityMainTopic(data: TopicData, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.login.email)
        val community = data.communityID?.let { rwCommunity.select(it) }
        lateinit var topic: Topic
        lateinit var topics: List<String>

        if (data.identifier != null && community != null && user != null) {

            topic = Topic(approved = data.login.email in community.getModerators(), id = UUID.randomUUID().toString(), creator = user.getEmail(), footer = user.getUserName(), mainTopic = null)
            topics = user.getTopics()
            topics.add(topic.getID())
            user.setTopics(topics)

            topics = community.getTopics()
            topics.add(topic.getID())
            community.setTopics(topics)

            rwTopic.insert(topic)
            rwUser.insert(user)
            rwCommunity.insert(community)
        }
    }

    private fun createCommunitySubTopic(data: TopicData, mainTopic: Topic, userDB: UserDBInterface, communityDB: CommunityDBInterface, topicDB: TopicDBInterface) {
        val user = userDB.select(data.login.email)
        val community = data.communityID?.let { communityDB.select(it) }
        lateinit var topic: Topic
        lateinit var comments: List<String>

        if (data.identifier != null && community != null && user != null && mainTopic.getApproved()) {
            topic = Topic(approved = true, id = UUID.randomUUID().toString(), creator = user.getEmail(), footer = user.getUserName(), mainTopic = mainTopic.getMainTopic())

            comments = mainTopic.getComments()
            comments.add(topic.getID())
            mainTopic.setComments(comments)

            topicDB.insert(mainTopic)
            topicDB.insert(topic)
            userDB.insert(user)
        }
    }

    private fun createUserMainTopic(data: TopicData, rwUser: UserDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.login.email)
        lateinit var topics: List<String>
        if (user != null) {
            val topic = Topic(approved = true, id = UUID.randomUUID().toString(), creator = user.getEmail(), footer = user.getUserName(), mainTopic = null)
            topics = user.getTopics()
            topics.add(topic.getID())
            user.setTopics(topics)
            
            rwTopic.insert(topic)
            rwUser.insert(user)
        }
    }

    private fun createUserSubTopic(data: TopicData, mainTopic: Topic, userDB: UserDBInterface, topicDB: TopicDBInterface) {
        val user = userDB.select(data.login.email)
        lateinit var topic: Topic
        lateinit var comments: List<String>
        if (data.identifier != null && user != null) {
            topic = Topic(approved = true, id = UUID.randomUUID().toString(), creator = user.getEmail(), footer = user.getUserName(), mainTopic = mainTopic.getID())

            comments = mainTopic.getComments()
            comments.add(topic.getID())
            mainTopic.setComments(comments)

            topicDB.insert(mainTopic)
            topicDB.insert(topic)
            userDB.insert(user)

        }
    }

}