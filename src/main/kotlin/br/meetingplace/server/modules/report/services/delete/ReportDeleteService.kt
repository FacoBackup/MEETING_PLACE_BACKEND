package br.meetingplace.server.modules.report.services.delete

import br.meetingplace.server.modules.report.dao.RI
import br.meetingplace.server.modules.report.dto.requests.RequestReportSimple

object ReportDeleteService {
    fun deleteReport(data: RequestReportSimple, reportDAO: RI) : Status {
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