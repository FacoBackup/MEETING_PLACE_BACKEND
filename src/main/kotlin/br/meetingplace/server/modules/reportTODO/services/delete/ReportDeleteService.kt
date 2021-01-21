package br.meetingplace.server.modules.reportTODO.services.delete

import br.meetingplace.server.modules.reportTODO.dao.RI
import br.meetingplace.server.modules.reportTODO.dto.requests.RequestReportSimple
import io.ktor.http.*

object ReportDeleteService {
    fun deleteReport(requester: Long,data: RequestReportSimple, reportDAO: RI) : HttpStatusCode {
        return try {
            val report = reportDAO.read(data.reportID)
            if(report != null && report.creatorID == requester)
                reportDAO.delete(data.reportID)
            else HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}