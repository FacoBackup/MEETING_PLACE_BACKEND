package br.meetingplace.server.services.community.classes.dependencies.topics

import br.meetingplace.server.services.community.classes.dependencies.data.Report
import br.meetingplace.server.services.topic.classes.SimplifiedTopic

interface CommunityTopicsInterface {
    fun getIdTopics(): List<SimplifiedTopic>
    fun getTopicsInValidation(): List<SimplifiedTopic>
    fun getReportedTopics(): List<Report>
    fun getIdReports(): List<String>
    fun checkTopicApproval(id: String): Boolean
    fun removeApprovedTopic(topic: SimplifiedTopic)
    fun updateTopicInValidation(topic: SimplifiedTopic, approve: Boolean?)
    fun updateReport(data: Report, delete: Boolean)

}