package br.meetingplace.server.modules.report.dao

import br.meetingplace.server.modules.report.dto.response.ReportDTO
import br.meetingplace.server.modules.report.dto.requests.RequestReportCreation
import br.meetingplace.server.response.status.Status

interface RI {
    fun create(data: RequestReportCreation): Status
    fun readAll(communityID: String, done: Boolean): List<ReportDTO>
    fun read(reportID: String): ReportDTO?
    fun update(reportID: String, reason: String?, response: String?, done: Boolean?): Status
    fun delete(reportID: String): Status
}