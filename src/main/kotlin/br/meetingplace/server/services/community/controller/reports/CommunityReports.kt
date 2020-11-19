package br.meetingplace.server.services.community.controller.reports

import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.ReportLSInterface
import br.meetingplace.server.loadstore.interfaces.TopicLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.community.ApprovalData
import br.meetingplace.server.dto.community.ReportData
import br.meetingplace.server.services.community.classes.dependencies.data.Report
import java.util.*

class CommunityReports private constructor() {
    companion object {
        private val Class = CommunityReports()
        fun getClass() = Class
    }

    fun createReport(data: ReportData, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface, rwTopic: TopicLSInterface, rwReport: ReportLSInterface) {
        val user = rwUser.load(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.load(it) }


        if (user != null && community != null && community.checkTopicApproval(data.identifier.ID)) {
            val topic = rwTopic.load(data.identifier.ID, null)
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
                rwCommunity.store(community)
                rwReport.store(newReport)
            }
        }
    }

    fun deleteReport(data: ApprovalData, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface, rwReport: ReportLSInterface) {
        val user = rwUser.load(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.load(it) }
        val report = rwReport.load(data.identifier.ID)

        if (user != null && community != null && report != null && (data.login.email in community.getModerators() || data.login.email == report.creator)) {
            community.updateReport(report, true)
            rwCommunity.store(community)
            rwReport.delete(report)
        }
    }
}