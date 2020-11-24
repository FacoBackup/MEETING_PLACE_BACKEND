package br.meetingplace.server.modules.user.dao.todo

import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.topic.classes.Topic
import br.meetingplace.server.requests.generic.data.Login

object UserReader {

    fun getMyTopics(data: Login, rwUser: UserDBInterface, rwTopic: TopicDBInterface): MutableList<Topic> {
        TODO("NOT YET IMPLEMENTED")
//        val user = rwUser.select(data.email)
//        val topics = mutableListOf<Topic>()
//        lateinit var topicIDs: List<String>
//
//        if (user != null) {
//            topicIDs = user.getTopics()
//
//            for (i in topicIDs.indices) {
//                val topic = rwTopic.select(topicIDs[i], null)
//                if (topic != null)
//                    topics.add(topic)
//            }
//            return topics
//        }
//        return topics
    }//READ

    fun getMyTimeline(data: Login, rwUser: UserDBInterface, rwTopic: TopicDBInterface): MutableList<Topic> { //NEEDS WORK HERE

        TODO("NOT YET IMPLEMENTED")
//        val user = rwUser.select(data.email)
//        val myTimeline = mutableListOf<Topic>()
//
//        if (user != null) {
//            val followingIds = user.getFollowing()
//
//            for (a in followingIds.indices) {
//                val following = rwUser.select(followingIds[a])
//                if (following != null) {
//                    val followingThreads = following.getMyTopics()
//                    for (element in followingThreads) {
//                        val topic = rwTopic.select(element, null)
//                        if (topic != null)
//                            myTimeline.add(topic)
//                    }
//                }
//            }
////
////            val communities = user.getCommunitiesIFollow()
////            for (i in communities.indices) {
////                val communities = rw.readCommunity(communities[i])
////                if (verify.verifyCommunity(community)) {
////                    val threads = community.getIdThreads()
////                    for (element in threads) {
////                        val thread = rw.readTopic(element.id, element.creator)
////                        if (verify.verifyTopic(thread))
////                            myTimeline.add(thread)
////                    }
////                }
////            }
//            return myTimeline
//        }
//        return myTimeline
    }//READ
}