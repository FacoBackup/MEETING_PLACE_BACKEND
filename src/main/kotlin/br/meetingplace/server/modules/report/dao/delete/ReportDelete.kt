package br.meetingplace.server.modules.report.dao.delete

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.report.ReportDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.report.dao.factory.ReportFactory
import br.meetingplace.server.requests.community.Approval

class ReportDelete private constructor(){

    companion object {
        private val Class = ReportDelete()
        fun getClass() = Class
    }

    fun deleteReport(data: Approval, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface, rwReport: ReportDBInterface) {
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