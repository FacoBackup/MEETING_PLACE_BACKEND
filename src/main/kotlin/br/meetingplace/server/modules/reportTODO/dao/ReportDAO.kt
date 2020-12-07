package br.meetingplace.server.modules.reportTODO.dao

import br.meetingplace.server.modules.reportTODO.dto.requests.RequestReportCreation
import br.meetingplace.server.modules.reportTODO.dto.response.ReportDTO
import br.meetingplace.server.modules.reportTODO.entities.Report
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*

object ReportDAO: RI {
    override fun create(data: RequestReportCreation): HttpStatusCode {
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
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun delete(reportID: String): HttpStatusCode {
        return try {
            transaction {
                Report.deleteWhere {
                    Report.id eq reportID
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
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

    override fun check(reportID: String): Boolean {
        return try {
            !transaction {
                Report.select {
                    Report.id eq reportID
                }.empty()
            }
        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
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

    override fun update(reportID: String, reason: String?, response: String?, done: Boolean?): HttpStatusCode {
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
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    private fun mapReport(it: ResultRow): ReportDTO {
        return ReportDTO(reportID =  it[Report.id], creationDate =  it[Report.creationDate].toString("dd-MM-yyyy"),
            creatorID = it[Report.creatorID] , reason =  it[Report.reason],
            response =  it[Report.response],done = it[Report.done],
            communityID =  it[Report.communityID], topicID = it[Report.topicID])
    }
}