package br.meetingplace.server.modules.report.dao

import br.meetingplace.server.modules.report.entitie.Report
import br.meetingplace.server.modules.report.dto.ReportDTO
import org.jetbrains.exposed.sql.ResultRow

object ReportMapper: ReportMapperInterface {
    override fun mapReport(it: ResultRow): ReportDTO {
        return ReportDTO(reportID =  it[Report.id], creationDate =  it[Report.creationDate].toString("dd-MM-yyyy"), creatorID = it[Report.creatorID] , reason =  it[Report.reason], response =  it[Report.response], status =  it[Report.status], communityID =  it[Report.communityID], topicID = it[Report.topicID])
    }
}