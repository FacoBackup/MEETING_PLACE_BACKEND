package br.meetingplace.server.modules.report.dao.delete

import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.modules.global.http.status.StatusMessages
import br.meetingplace.server.modules.report.db.Report
import br.meetingplace.server.requests.report.ReportOperator
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

object ReportDelete {
    fun deleteReport(data: ReportOperator) : Status {
        return try {
            Report.deleteWhere { (Report.creatorID eq data.userID) and (Report.id eq data.reportID)}
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}