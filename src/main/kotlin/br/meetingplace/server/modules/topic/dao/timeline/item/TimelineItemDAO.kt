package br.meetingplace.server.modules.topic.dao.timeline.item

import br.meetingplace.server.modules.topic.dto.response.TimelineItemDTO
import br.meetingplace.server.modules.topic.entities.timeline.TimelineItemEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object TimelineItemDAO:TMII {

    override suspend fun create(topicID: Long, userID: Long): HttpStatusCode {
        return try{
            transaction{
                TimelineItemEntity.deleteWhere { (TimelineItemEntity.userID eq userID) and (TimelineItemEntity.validUntil.less(System.currentTimeMillis())) }
                TimelineItemEntity.insert{
                    it[this.topicID] = topicID
                    it[this.userID] = userID
                    it[this.validUntil] = System.currentTimeMillis() + 86400000
                }
            }
            HttpStatusCode.Created
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override suspend fun readNewest(userID: Long): List<TimelineItemDTO> {
        return try{
            transaction{
                TimelineItemEntity.deleteWhere { (TimelineItemEntity.userID eq userID) and (TimelineItemEntity.validUntil.less(System.currentTimeMillis())) }
                TimelineItemEntity.select{
                    (TimelineItemEntity.userID eq userID)
                }.limit(5).orderBy(TimelineItemEntity.topicID, SortOrder.DESC).map { mapItem(it) }
            }
        }catch (e: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun readByMaxID(userID: Long, maxID: Long): List<TimelineItemDTO> {
        return try{
            transaction{
                TimelineItemEntity.deleteWhere { (TimelineItemEntity.userID eq userID) and (TimelineItemEntity.validUntil.less(System.currentTimeMillis())) }
                TimelineItemEntity.select{
                    TimelineItemEntity.topicID.less(maxID) and (TimelineItemEntity.userID eq userID)
                }.limit(5).orderBy(TimelineItemEntity.topicID, SortOrder.DESC).map { mapItem(it) }
            }
        }catch (e: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun read(userID: Long): List<TimelineItemDTO> {
        return try{
            transaction{
                TimelineItemEntity.deleteWhere { (TimelineItemEntity.userID eq userID) and (TimelineItemEntity.validUntil.less(System.currentTimeMillis())) }
                TimelineItemEntity.select{
                    TimelineItemEntity.userID eq userID
                }.map { mapItem(it) }
            }
        }catch (e: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    private fun mapItem(it: ResultRow): TimelineItemDTO{
        return TimelineItemDTO(
            userID = it[TimelineItemEntity.userID],
            topicID = it[TimelineItemEntity.topicID],
        )
    }
}