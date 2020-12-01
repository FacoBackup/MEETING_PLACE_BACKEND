package br.meetingplace.server.modules.report.service.delete

import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.report.entitie.Report
import br.meetingplace.server.request.dto.report.ReportSimpleDTO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

object ReportDelete {
    fun deleteReport(data: ReportSimpleDTO) : Status {
        return try {
            transaction {
                Report.deleteWhere { (Report.creatorID eq data.userID) and (Report.id eq data.reportID)}
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}