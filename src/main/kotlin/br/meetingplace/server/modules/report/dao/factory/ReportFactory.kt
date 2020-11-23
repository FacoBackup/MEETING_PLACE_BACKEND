package br.meetingplace.server.modules.report.dao.factory

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.report.ReportDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.report.dto.Report
import br.meetingplace.server.requests.community.ReportCreationData
import java.util.*

class ReportFactory private constructor() {
    companion object {
        private val Class = ReportFactory()
        fun getClass() = Class
    }

    fun createReport(data: ReportCreationData, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface, rwTopic: TopicDBInterface, rwReport: ReportDBInterface): Status {
        val user = rwUser.select(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.select(it) }
        val topic = rwTopic.select(data.identifier.ID, null)
        lateinit var reports: List<String>

        return if (user != null && community != null && topic != null && topic.getID() in community.getTopics()) {
            reports = community.getReports()
            val newReport = Report(
                    reportID = UUID.randomUUID().toString(),
                    creator = data.login.email,
                    serviceID = data.identifier.ID,
                    reason = data.reason,
                    finished = false,
                    communityId = community.getID(),
                    response = null
            )

            reports.add(newReport.reportID)
            community.setReports(reports)

            rwCommunity.insert(community)
            return rwReport.insert(newReport)
        } else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

}