package br.meetingplace.server.modules.report.dao

import br.meetingplace.server.modules.report.entitie.Report
import br.meetingplace.server.modules.report.dto.ReportDTO
import br.meetingplace.server.request.dto.report.ReportCreationDTO
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*

object ReportDAO: RI {
    override fun create(data: ReportCreationDTO): Status {
        return try {
            transaction {
                Report.insert {
                    it[id] = UUID.randomUUID().toString()
                    it[creatorID] = data.userID
                    it[topicID] = data.topicID
                    it[communityID] = data.communityID
                    it[creationDate] =  DateTime.now()
                    it[response] = null
                    it[done] = false
                    it[reason] = data.reason
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(reportID: String): Status {
        return try {
            transaction {
                Report.deleteWhere {
                    Report.id eq reportID
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(reportID: String): ReportDTO? {
        return try {
            transaction {
                Report.select {
                    Report.id eq reportID
                }.map { mapReport(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override fun readAll(communityID: String, done: Boolean): List<ReportDTO> {
        return try {
            transaction {
                Report.select {
                    (Report.communityID eq communityID) and
                    (Report.done eq done)
                }.map { mapReport(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }

    override fun update(reportID: String, reason: String?, response: String?, done: Boolean?): Status {
        return try {
            transaction {
                Report.update({Report.id eq reportID}){
                    if(!reason.isNullOrBlank())
                        it[this.reason] = reason
                    if(!response.isNullOrBlank())
                        it[this.response] = response
                    if(done != null)
                        it[this.done] = done
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    private fun mapReport(it: ResultRow): ReportDTO {
        return ReportDTO(reportID =  it[Report.id], creationDate =  it[Report.creationDate].toString("dd-MM-yyyy"),
            creatorID = it[Report.creatorID] , reason =  it[Report.reason],
            response =  it[Report.response],done = it[Report.done],
            communityID =  it[Report.communityID], topicID = it[Report.topicID])
    }
}