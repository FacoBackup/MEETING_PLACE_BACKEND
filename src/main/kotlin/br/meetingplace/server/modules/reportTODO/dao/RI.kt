package br.meetingplace.server.modules.reportTODO.dao

import br.meetingplace.server.modules.reportTODO.dto.requests.RequestReportCreation
import br.meetingplace.server.modules.reportTODO.dto.response.ReportDTO
import io.ktor.http.*

interface RI {
    fun create(requester: Long, data: RequestReportCreation): HttpStatusCode
    fun readAll(communityID: Long, done: Boolean): List<ReportDTO>
    fun read(reportID: Long): ReportDTO?
    fun check(reportID: Long): Boolean
    fun update(reportID: Long, reason: String?, response: String?, done: Boolean?): HttpStatusCode
    fun delete(reportID: Long): HttpStatusCode
}