package br.meetingplace.server.db.mapper.report

import br.meetingplace.server.modules.report.db.Report
import br.meetingplace.server.modules.report.dto.ReportDTO
import org.jetbrains.exposed.sql.ResultRow

class ReportMapper: ReportMapperInterface {
    override fun mapReport(it: ResultRow): ReportDTO {
        return ReportDTO(reportID =  it[Report.id], creationDate =  it[Report.creationDate].toString("dd-MM-yyyy"), creatorID = it[Report.creatorID] , reason =  it[Report.reason], response =  it[Report.response], status =  it[Report.status], communityID =  it[Report.communityID], topicID = it[Report.topicID])
    }
}