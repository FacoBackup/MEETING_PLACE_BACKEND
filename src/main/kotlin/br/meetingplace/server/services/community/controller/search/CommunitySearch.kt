package br.meetingplace.server.services.community.controller.search

import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.TopicLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.services.community.classes.dependencies.data.Report
import br.meetingplace.server.services.members.data.MemberType
import br.meetingplace.server.services.topic.classes.SimplifiedTopic
import br.meetingplace.server.services.topic.classes.Topic

class CommunitySearch {

    fun seeReports(data: SimpleOperator, rwCommunity: CommunityLSInterface, rwUser: UserLSInterface): List<Report> {
        val user = rwUser.load(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.load(it) }

        if (user != null && community != null &&
                (community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR))
            return community.getReportedTopics()

        return listOf()
    }

    fun seeMembers(data: SimpleOperator, rwCommunity: CommunityLSInterface, rwUser: UserLSInterface): List<String> {
        val user = rwUser.load(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.load(it) }

        if (user != null && community != null)
            return community.getMembers()

        return listOf()
    }

    fun seeThreads(data: SimpleOperator, rwTopic: TopicLSInterface, rwCommunity: CommunityLSInterface, rwUser: UserLSInterface): List<Topic> {
        val user = rwUser.load(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.load(it) }
        lateinit var topics: List<SimplifiedTopic>

        if (user != null && community != null) {
            topics = community.getIdTopics()

            val communityTopics = mutableListOf<Topic>()

            for (element in topics) {
                val topic = rwTopic.load(element.ID, null)
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