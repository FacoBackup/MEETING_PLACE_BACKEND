package br.meetingplace.server.modules.topic.dao

import br.meetingplace.server.modules.topic.entities.Topic
import br.meetingplace.server.modules.topic.dto.response.TopicDTO
import br.meetingplace.server.modules.topic.dto.requests.RequestTopicCreation
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*

object TopicDAO: TI {

    override fun create(data: RequestTopicCreation, approved:Boolean, userName: String): Status {
        return try {
            transaction {
                Topic.insert {
                    it[id] = UUID.randomUUID().toString()
                    it[header] = data.header
                    it[body] = data.body
                    it[imageURL] = data.imageURL
                    it[Topic.approved] = approved
                    it[footer] = userName
                    it[creatorID] = data.userID
                    it[mainTopicID] = data.mainTopicID
                    it[communityID] = data.communityID
                    it[creationDate] = DateTime.now()
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(topicID: String): Status {
        return try {
            transaction {
                Topic.deleteWhere {
                    Topic.id eq topicID
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(topicID: String): TopicDTO? {
        return try {
            transaction {
                Topic.select {
                    Topic.id eq topicID
                }.map { mapTopic(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    override fun readByUser(userID: String): List<TopicDTO> {
        return try {
            transaction {
                Topic.select {
                    (Topic.creatorID eq userID) and
                    (Topic.approved eq true) and
                    (Topic.mainTopicID eq null)
                }.map { mapTopic(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override fun readAllComments(topicID: String): List<TopicDTO> {
        return try {
            transaction {
                Topic.select {
                    Topic.mainTopicID eq topicID
                }.map { mapTopic(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override fun update(
        topicID: String,
        approved: Boolean?,
        mainTopicID: String?,
        header: String?,
        body: String?
    ): Status {
        return try {
            transaction {

            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    private fun mapTopic(it: ResultRow): TopicDTO {
        return TopicDTO(id =  it[Topic.id], header =  it[Topic.header], body =  it[Topic.body], approved =  it[Topic.approved], footer =  it[Topic.footer], creatorID =  it[Topic.creatorID], mainTopicID =  it[Topic.mainTopicID], creationDate =  it[Topic.creationDate].toString("dd-MM-yyyy"), communityID = it[Topic.communityID], imageURL = it[Topic.imageURL])
    }
}