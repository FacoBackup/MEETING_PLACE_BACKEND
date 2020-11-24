package br.meetingplace.server.modules.report.dao.delete

import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.requests.community.Approval

object ReportDelete {

    fun deleteReport(data: Approval, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface, rwReport: ReportDBInterface) : Status {
        TODO("Not yet implemented")
    }
}