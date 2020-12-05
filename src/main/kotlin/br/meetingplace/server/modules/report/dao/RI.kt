package br.meetingplace.server.modules.report.dao

import br.meetingplace.server.modules.report.dto.response.ReportDTO
import br.meetingplace.server.modules.report.dto.requests.RequestReportCreation
import io.ktor.http.*

interface RI {
    fun create(data: RequestReportCreation): HttpStatusCode
    fun readAll(communityID: String, done: Boolean): List<ReportDTO>
    fun read(reportID: String): ReportDTO?
    fun check(reportID: String): HttpStatusCode
    fun update(reportID: String, reason: String?, response: String?, done: Boolean?): HttpStatusCode
    fun delete(reportID: String): HttpStatusCode
}