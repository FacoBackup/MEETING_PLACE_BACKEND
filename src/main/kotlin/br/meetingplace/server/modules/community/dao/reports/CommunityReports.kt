package br.meetingplace.server.modules.community.dao.reports

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.report.ReportDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.community.dto.dependencies.data.Report
import br.meetingplace.server.requests.community.ApprovalData
import br.meetingplace.server.requests.community.ReportData
import java.util.*

class CommunityReports private constructor() {
    companion object {
        private val Class = CommunityReports()
        fun getClass() = Class
    }

    fun createReport(data: ReportData, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface, rwTopic: TopicDBInterface, rwReport: ReportDBInterface) {
        val user = rwUser.select(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.select(it) }


        if (user != null && community != null && community.checkTopicApproval(data.identifier.ID)) {
            val topic = rwTopic.select(data.identifier.ID, null)
            if (topic != null) {
                val newReport = Report(
                        UUID.randomUUID().toString(),
                        data.login.email,
                        data.identifier.ID,
                        data.reason,
                        false,
                        community.getID(),
                        null
                )
                community.updateReport(newReport, false)
                rwCommunity.insert(community)
                rwReport.insert(newReport)
            }
        }
    }

    fun deleteReport(data: ApprovalData, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface, rwReport: ReportDBInterface) {
        val user = rwUser.select(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.select(it) }
        val report = rwReport.select(data.identifier.ID)

        if (user != null && community != null && report != null && (data.login.email in community.getModerators() || data.login.email == report.creator)) {
            community.updateReport(report, true)
            rwCommunity.insert(community)
            rwReport.delete(report)
        }
    }
}