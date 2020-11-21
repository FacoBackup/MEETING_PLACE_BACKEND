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
        // TODO: 21/11/2020
    }
}