package br.meetingplace.server.modules.topic.dao.tag

import br.meetingplace.server.modules.topic.dto.response.TagDTO
import br.meetingplace.server.modules.topic.dto.response.TopicTagDTO
import br.meetingplace.server.modules.topic.entities.tags.TagEntity
import br.meetingplace.server.modules.topic.entities.tags.TopicTagEntity
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


    override suspend fun read(tag: String): TagDTO? {
        return try {
            transaction {
                TagEntity.select{ TagEntity.tag like "%$tag%"}.map{ mapTagDTO(it, 0)}.firstOrNull()
            }
        }catch (e: Exception){
            null
        }catch (PSQL: PSQLException){
            null
        }
    }

    override suspend fun readRank(): List<TagDTO>{
        return try {

            val tags = mutableListOf<TagDTO>()
            transaction {
                (TagEntity innerJoin TopicTagEntity)
                    .slice(TagEntity.tag, TagEntity.tagID, TopicTagEntity.topicID.count())
                    .selectAll()
                    .groupBy(TagEntity.tagID)
                    .limit(4)
                    .forEach{
                        tags.add(mapTagDTO(it, it[TopicTagEntity.topicID.count()]))
                    }
            }

            (tags.toList()).sortedBy { it.numberOfTopics } .reversed()
        }catch (e: Exception){
            println(e.message)
            listOf()
        }catch (PSQL: PSQLException){
            println(PSQL.message)
            listOf()
        }
    }

    override suspend fun readByMaxID(tagID: Long, maxID: Long): List<TopicTagDTO> {
        return try {

            val tags = mutableListOf<TopicTagDTO>()
            transaction {
                (TagEntity innerJoin TopicTagEntity)
                    .slice(TopicTagEntity.tagID, TopicTagEntity.topicID)
                    .select{
                        TopicTagEntity.tagID.eq(tagID) and TopicTagEntity.topicID.less(maxID)
                    }
                    .limit(5)
                    .forEach{
                        tags.add(mapTopicTagDTO(it))
                    }
            }

            (tags.toList()).sortedBy { it.topicID } .reversed()
        }catch (e: Exception){
            println(e.message)
            listOf()
        }catch (PSQL: PSQLException){
            println(PSQL.message)
            listOf()
        }
    }

    override suspend fun readNewest(tagID: Long): List<TopicTagDTO> {
        return try {

            val tags = mutableListOf<TopicTagDTO>()
            transaction {
                (TagEntity innerJoin TopicTagEntity)
                    .slice(TopicTagEntity.tagID, TopicTagEntity.topicID)
                    .select{
                        TopicTagEntity.tagID.eq(tagID)
                    }
                    .limit(5)
                    .forEach{
                        tags.add(mapTopicTagDTO(it))
                    }
            }

            (tags.toList()).sortedBy { it.topicID } .reversed()
        }catch (e: Exception){
            println(e.message)
            listOf()
        }catch (PSQL: PSQLException){
            println(PSQL.message)
            listOf()
        }
    }
    private fun mapTopicTagDTO(it: ResultRow): TopicTagDTO{
        return TopicTagDTO(
            topicID = it[TopicTagEntity.topicID],
            tagID = it[TopicTagEntity.tagID]
        )
    }
    private fun mapTagDTO(it: ResultRow, quantity: Long): TagDTO{
        return TagDTO(
            numberOfTopics =quantity,
            tagID = it[TagEntity.tagID],
            tagValue = it[TagEntity.tag],
        )
    }
}