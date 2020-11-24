package br.meetingplace.server.db.mapper.report

import br.meetingplace.server.modules.report.dto.ReportDTO
import br.meetingplace.server.modules.user.dto.UserDTO
import org.jetbrains.exposed.sql.ResultRow

interface ReportMapperInterface {
    fun mapReport(it: ResultRow): ReportDTO
}