package br.meetingplace.server.subjects.services.community.dependencies

import br.meetingplace.server.subjects.services.community.dependencies.data.Report
import br.meetingplace.server.subjects.services.community.dependencies.groups.CommunityGroups
import br.meetingplace.server.subjects.services.community.dependencies.groups.CommunityGroupsInterface
import br.meetingplace.server.subjects.services.community.dependencies.topics.CommunityTopics
import br.meetingplace.server.subjects.services.community.dependencies.topics.CommunityTopicsInterface
import br.meetingplace.server.subjects.services.members.Members
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic

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