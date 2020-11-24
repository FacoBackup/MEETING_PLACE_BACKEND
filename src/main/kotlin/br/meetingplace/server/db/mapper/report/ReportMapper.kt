package br.meetingplace.server.db.mapper.report

import br.meetingplace.server.modules.report.dto.ReportDTO
import org.jetbrains.exposed.sql.ResultRow

class ReportMapper: ReportMapperInterface {
    override fun mapReport(it: ResultRow): ReportDTO {
        TODO("Not yet implemented")
    }
}