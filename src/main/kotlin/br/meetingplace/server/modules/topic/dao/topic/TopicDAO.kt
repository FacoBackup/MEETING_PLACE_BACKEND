package br.meetingplace.server.modules.topic.dao.topic

import br.meetingplace.server.modules.topic.dto.response.TopicDTO
import br.meetingplace.server.modules.topic.entities.TopicEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException
import java.util.*

object TopicDAO: TI {
    override suspend fun readQuantityComments(topicID: String): Long {
        return try {
            transaction {
                TopicEntity.select {
                    (TopicEntity.mainTopicID eq topicID)
                }.count()
            }
        }catch (normal: Exception){
            0
        }catch (psql: PSQLException){
            0
        }
    }

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

    override suspend fun readByTimePeriod(subjectID: String, since: Long, community: Boolean): List<TopicDTO> {
        return try{
            transaction {
                TopicEntity.select{
                    when(community){
                        true->{
                            TopicEntity.creationDate.lessEq(since) and
                            (TopicEntity.communityID eq subjectID)
                        }
                        false->{
                            TopicEntity.creationDate.lessEq(since) and
                            (TopicEntity.creatorID eq subjectID)
                        }
                    }

                }.limit(5).map { mapTopic(it) }
            }
        }catch (e: Exception){
            listOf()
        }catch(psql: PSQLException){
            listOf()
        }
    }

    override suspend fun readTopicsQuantityByCommunity(communityID: String): Long {
        return try{
            transaction {
                TopicEntity.select{
                    (TopicEntity.communityID eq communityID) and
                            (TopicEntity.mainTopicID eq null)
                }.count()
            }
        }catch (e: Exception){
            0
        }catch(psql: PSQLException){
            0
        }
    }
    override suspend fun readTopicsQuantityByUser(userID: String): Long {
        return try{
            transaction {
                TopicEntity.select{
                    (TopicEntity.creatorID eq userID) and
                            (TopicEntity.mainTopicID eq null)
                }.count()
            }
        }catch (e: Exception){
            0
        }catch(psql: PSQLException){
            0
        }
    }
    override suspend fun create(header: String,
                        body: String?,
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
    override suspend fun readBySubject(subjectID: String, timePeriod: Long, community: Boolean): List<TopicDTO> {
        return try {
            transaction {
                TopicEntity.select {
                    when(community){
                        true->{
                            (TopicEntity.communityID eq subjectID) and
                            (TopicEntity.approved eq true) and
                            (TopicEntity.mainTopicID eq null) and
                                    TopicEntity.creationDate.lessEq(timePeriod)
                        }
                        false->{
                            (TopicEntity.creatorID eq subjectID) and
                            (TopicEntity.approved eq true) and
                            (TopicEntity.mainTopicID eq null) and
                                    TopicEntity.creationDate.lessEq(timePeriod)
                        }
                    }

                }.limit(10).map { mapTopic(it) }
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
        header: String?,
        body: String?
    ): HttpStatusCode {

        return try {
            transaction {
                TopicEntity.update ({
                    (TopicEntity.id eq topicID)
                }){
                    if(approved != null)
                        it[this.approved] = approved
                    if(!header.isNullOrBlank())
                        it[this.header] = header

                    it[this.body] = body
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
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