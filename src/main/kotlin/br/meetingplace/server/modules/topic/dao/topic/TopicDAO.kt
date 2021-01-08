package br.meetingplace.server.modules.topic.dao.topic

import br.meetingplace.server.modules.topic.dto.response.TopicDTO
import br.meetingplace.server.modules.topic.entities.TopicEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException
import java.util.*

object TopicDAO: TI {
    override suspend fun read(topicID: String): TopicDTO? {
        return try {
            transaction {
                TopicEntity.select {
                    TopicEntity.id eq topicID
                }.map { mapTopic(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override suspend fun readByTimePeriod(subjectID: String, until: Long, community: Boolean): List<TopicDTO> {
        return try{
            transaction {
                TopicEntity.select{
                    when(community){
                        true->{
                            TopicEntity.creationDate.greaterEq(until) and
                            (TopicEntity.communityID eq subjectID)
                        }
                        false->{
                            TopicEntity.creationDate.greaterEq(until) and
                            (TopicEntity.creatorID eq subjectID)
                        }
                    }

                }.map { mapTopic(it) }
            }
        }catch (e: Exception){
            listOf()
        }catch(psql: PSQLException){
            listOf()
        }
    }
    override suspend fun create(header: String,
                        body: String,
                        imageURL: String?,
                        communityID: String?,
                        userID:String,
                        mainTopicID: String?,
                        approved:Boolean,
                        userName: String): HttpStatusCode {
        return try {
            transaction {
                TopicEntity.insert {
                    it[id] = UUID.randomUUID().toString()
                    it[this.header] = header
                    it[this.body] = body
                    it[this.imageURL] = imageURL
                    it[TopicEntity.approved] = approved
                    it[footer] = userName
                    it[creatorID] = userID
                    it[this.mainTopicID] = mainTopicID
                    it[this.communityID] = communityID
                    it[creationDate] = System.currentTimeMillis()
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            println("NORMAL EXCEPTION" + normal.message)
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            println("PSQL EXCEPTION" + psql.message)
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun delete(topicID: String):HttpStatusCode{
        return try {
            transaction {
                TopicEntity.deleteWhere {
                    TopicEntity.id eq topicID
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun check(topicID: String): Boolean {
        return try {
            return !transaction {
                TopicEntity.select {
                    TopicEntity.id eq topicID
                }.empty() }
        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }
    override suspend fun readBySubject(subjectID: String, community: Boolean): List<TopicDTO> {
        return try {
            transaction {
                TopicEntity.select {
                    when(community){
                        true->{
                            (TopicEntity.communityID eq subjectID) and
                            (TopicEntity.approved eq true) and
                            (TopicEntity.mainTopicID eq null)
                        }
                        false->{
                            (TopicEntity.creatorID eq subjectID) and
                            (TopicEntity.approved eq true) and
                            (TopicEntity.mainTopicID eq null)
                        }
                    }

                }.map { mapTopic(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun readAllComments(topicID: String): List<TopicDTO> {
        return try {
            transaction {
                TopicEntity.select {
                    TopicEntity.mainTopicID eq topicID
                }.map { mapTopic(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun update(
        topicID: String,
        approved: Boolean?,
        mainTopicID: String?,
        header: String?,
        body: String?
    ): HttpStatusCode {
        TODO()
//        return try {
//            transaction {
//
//            }
//            HttpStatusCode.OK
//        }catch (normal: Exception){
//            HttpStatusCode.InternalServerError
//        }catch (psql: PSQLException){
//            HttpStatusCode.InternalServerError
//        }
    }
    private fun mapTopic(it: ResultRow): TopicDTO {
        return TopicDTO(
            id =  it[TopicEntity.id],
            header =  it[TopicEntity.header],
            body =  it[TopicEntity.body],
            approved =  it[TopicEntity.approved],
            creatorID =  it[TopicEntity.creatorID],
            mainTopicID =  it[TopicEntity.mainTopicID],
            creationDate =  it[TopicEntity.creationDate],
            communityID = it[TopicEntity.communityID],
            imageURL = it[TopicEntity.imageURL])
    }
}