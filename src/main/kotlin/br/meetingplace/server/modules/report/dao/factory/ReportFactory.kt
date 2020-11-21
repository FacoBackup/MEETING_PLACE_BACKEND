package br.meetingplace.server.modules.report.dao.factory

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.report.ReportDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.report.dto.Report
import br.meetingplace.server.requests.community.Approval
import br.meetingplace.server.requests.community.ReportCreationData
import java.util.*

class ReportFactory private constructor() {
    companion object {
        private val Class = ReportFactory()
        fun getClass() = Class
    }

    fun createReport(data: ReportCreationData, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface, rwTopic: TopicDBInterface, rwReport: ReportDBInterface) {
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

}