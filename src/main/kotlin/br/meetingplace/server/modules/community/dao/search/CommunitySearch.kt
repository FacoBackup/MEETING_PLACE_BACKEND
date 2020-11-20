package br.meetingplace.server.modules.community.dao.search

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.community.dto.dependencies.data.Report
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.modules.topic.dto.SimplifiedTopic
import br.meetingplace.server.modules.topic.dto.Topic
import br.meetingplace.server.requests.generic.SimpleOperator

class CommunitySearch {

    fun seeReports(data: SimpleOperator, rwCommunity: CommunityDBInterface, rwUser: UserDBInterface): List<Report> {
        val user = rwUser.select(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.select(it) }

        if (user != null && community != null &&
                (community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR))
            return community.getReportedTopics()

        return listOf()
    }

    fun seeMembers(data: SimpleOperator, rwCommunity: CommunityDBInterface, rwUser: UserDBInterface): List<String> {
        val user = rwUser.select(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.select(it) }

        if (user != null && community != null)
            return community.getMembers()

        return listOf()
    }

    fun seeThreads(data: SimpleOperator, rwTopic: TopicDBInterface, rwCommunity: CommunityDBInterface, rwUser: UserDBInterface): List<Topic> {
        val user = rwUser.select(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.select(it) }
        lateinit var topics: List<SimplifiedTopic>

        if (user != null && community != null) {
            topics = community.getIdTopics()

            val communityTopics = mutableListOf<Topic>()

            for (element in topics) {
                val topic = rwTopic.select(element.ID, null)
                if (topic != null)
                    communityTopics.add(topic)
            }

            return communityTopics
        }

        return listOf()
    }

//    fun seeGroups(data: SimpleOperator): List<Group> {
////        val logged = rw.readLoggedUser().email
////        val user = rw.readUser(logged)
////        val community = rw.readCommunity(iDs.getCommunityId(data.ID))
////
////        if (verify.verifyUser(user) && verify.verifyCommunity(community)){
////            val groupIDs = community.getApprovedGroups()
////            //for (i in groupIDs.indices)
////
////        }
////        return listOf()
//
//        TODO("NOT IMPLEMENTED YET")
//    }
}