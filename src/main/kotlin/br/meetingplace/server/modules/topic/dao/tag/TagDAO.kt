package br.meetingplace.server.modules.topic.dao.tag

import br.meetingplace.server.modules.topic.dto.response.TopicTagDTO
import br.meetingplace.server.modules.topic.entities.tags.TagEntity
import br.meetingplace.server.modules.topic.entities.tags.TopicTagEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object TagDAO: TagInterface {
    override suspend fun create(tag: String): Long? {
        return try {
            transaction {
                TagEntity.insert {
                    it[this.tag] = tag
                    it[creationDate] = System.currentTimeMillis()
                } get TagEntity.tagID
            }

        }catch (e: Exception){
            null
        }catch (PSQL: PSQLException){
            null
        }
    }


    override suspend fun read(tag: String): TopicTagDTO? {
        return try {
            transaction {
                TagEntity.select{ TagEntity.tag like "%$tag%"}.map{ mapTopicTagDTO(it, 0)}.firstOrNull()
            }
        }catch (e: Exception){
            null
        }catch (PSQL: PSQLException){
            null
        }
    }

    override suspend fun readRank(): List<TopicTagDTO>{
        return try {
            val tags = mutableListOf<TopicTagDTO>()
            transaction {
                (TagEntity innerJoin TopicTagEntity)
                    .slice(TagEntity.tagID, TopicTagEntity.topicID.count())
                    .selectAll()
                    .groupBy(TagEntity.tagID)
                    .limit(6)
                    .forEach{
                    tags.add(mapTopicTagDTO(it, it[TopicTagEntity.tagID.count()]))
                }
            }
            tags
        }catch (e: Exception){
            listOf()
        }catch (PSQL: PSQLException){
            listOf()
        }
    }

    private fun mapTopicTagDTO(it: ResultRow, quantity: Long): TopicTagDTO{
        return TopicTagDTO(
            numberOfTopics =quantity,
            tagID = it[TagEntity.tagID],
            tagValue = it[TagEntity.tag],
        )
    }
}