package br.meetingplace.server.modules.community.dto.dependencies

import br.meetingplace.server.modules.community.dto.dependencies.data.Report
import br.meetingplace.server.modules.community.dto.dependencies.groups.CommunityGroups
import br.meetingplace.server.modules.community.dto.dependencies.groups.CommunityGroupsInterface
import br.meetingplace.server.modules.community.dto.dependencies.topics.CommunityTopics
import br.meetingplace.server.modules.community.dto.dependencies.topics.CommunityTopicsInterface
import br.meetingplace.server.modules.members.dao.Members
import br.meetingplace.server.modules.topic.dto.SimplifiedTopic

abstract class Controller : Members(), CommunityTopicsInterface, CommunityGroupsInterface {

    private val topics = CommunityTopics.getClass()
    private val groups = CommunityGroups.getClass()

    override fun checkGroupApproval(id: String): Boolean {
        return groups.checkGroupApproval(id)
    }

    override fun checkTopicApproval(id: String): Boolean {
        return topics.checkTopicApproval(id)
    }

    override fun getApprovedGroups(): List<String> {
        return groups.getApprovedGroups()
    }

    override fun getGroupsInValidation(): List<String> {
        return groups.getGroupsInValidation()
    }

    override fun getIdReports(): List<String> {
        return topics.getIdReports()
    }

    override fun getIdTopics(): List<SimplifiedTopic> {
        return topics.getIdTopics()
    }

    override fun getReportedTopics(): List<Report> {
        return topics.getReportedTopics()
    }

    override fun getTopicsInValidation(): List<SimplifiedTopic> {
        return topics.getTopicsInValidation()
    }

    override fun removeApprovedGroup(group: String) {
        groups.removeApprovedGroup(group)
    }

    override fun removeApprovedTopic(topic: SimplifiedTopic) {
        topics.removeApprovedTopic(topic)
    }

    override fun updateGroupsInValidation(group: String, approve: Boolean?) {
        groups.updateGroupsInValidation(group, approve)
    }

    override fun updateReport(data: Report, delete: Boolean) {
        topics.updateReport(data, delete)
    }

    override fun updateTopicInValidation(topic: SimplifiedTopic, approve: Boolean?) {
        topics.updateTopicInValidation(topic, approve)
    }
}