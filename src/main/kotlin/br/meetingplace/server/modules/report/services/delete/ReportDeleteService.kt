package br.meetingplace.server.modules.report.services.delete

import br.meetingplace.server.modules.report.dao.RI
import br.meetingplace.server.modules.report.dto.requests.RequestReportSimple
import io.ktor.http.*

object ReportDeleteService {
    fun deleteReport(data: RequestReportSimple, reportDAO: RI) : HttpStatusCode {
        return try {
            val report = reportDAO.read(data.reportID)
            if(report != null && report.creatorID == data.userID)
                reportDAO.delete(data.reportID)
            else HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}