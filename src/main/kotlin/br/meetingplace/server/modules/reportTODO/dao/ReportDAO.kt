package br.meetingplace.server.modules.reportTODO.dao

import br.meetingplace.server.modules.reportTODO.dto.requests.RequestReportCreation
import br.meetingplace.server.modules.reportTODO.dto.response.ReportDTO
import br.meetingplace.server.modules.reportTODO.entities.ReportEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*

object ReportDAO: RI {
    override fun create(requester: Long, data: RequestReportCreation): HttpStatusCode {
        return try {
            transaction {
                ReportEntity.insert {
                    it[creatorID] = requester
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

    override fun delete(reportID: Long): HttpStatusCode {
        return try {
            transaction {
                ReportEntity.deleteWhere {
                    ReportEntity.id eq reportID
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun read(reportID: Long): ReportDTO? {
        return try {
            transaction {
                ReportEntity.select {
                    ReportEntity.id eq reportID
                }.map { mapReport(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override fun check(reportID: Long): Boolean {
        return try {
            !transaction {
                ReportEntity.select {
                    ReportEntity.id eq reportID
                }.empty()
            }
        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }
    override fun readAll(communityID: Long, done: Boolean): List<ReportDTO> {
        return try {
            transaction {
                ReportEntity.select {
                    (ReportEntity.communityID eq communityID) and
                    (ReportEntity.done eq done)
                }.map { mapReport(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }

    override fun update(reportID: Long, reason: String?, response: String?, done: Boolean?): HttpStatusCode {
        return try {
            transaction {
                ReportEntity.update({ReportEntity.id eq reportID}){
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
        return ReportDTO(
            reportID =  it[ReportEntity.id],
            creationDate =  it[ReportEntity.creationDate].toString("dd-MM-yyyy"),
            creatorID = it[ReportEntity.creatorID] ,
            reason =  it[ReportEntity.reason],
            response =  it[ReportEntity.response],
            done = it[ReportEntity.done],
            communityID =  it[ReportEntity.communityID],
            topicID = it[ReportEntity.topicID],
            responseCreatorID = it[ReportEntity.responseCreator]
            )
    }
}