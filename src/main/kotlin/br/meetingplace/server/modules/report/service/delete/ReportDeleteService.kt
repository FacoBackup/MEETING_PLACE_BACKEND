package br.meetingplace.server.modules.report.service.delete

import br.meetingplace.server.modules.report.dao.RI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.report.entitie.Report
import br.meetingplace.server.request.dto.report.ReportSimpleDTO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

object ReportDeleteService {
    fun deleteReport(data: ReportSimpleDTO, reportDAO: RI) : Status {
        return try {
            val report = reportDAO.read(data.reportID)
            if(report != null && report.creatorID == data.userID)
                reportDAO.delete(data.reportID)
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}