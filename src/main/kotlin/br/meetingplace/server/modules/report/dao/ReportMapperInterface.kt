package br.meetingplace.server.modules.report.dao

import br.meetingplace.server.modules.report.dto.ReportDTO
import org.jetbrains.exposed.sql.ResultRow

interface ReportMapperInterface {
    fun mapReport(it: ResultRow): ReportDTO
}