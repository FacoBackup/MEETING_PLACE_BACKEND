package br.meetingplace.server.modules.topic.service.factory

import br.meetingplace.server.modules.community.dao.CommunityDAOInterface
import br.meetingplace.server.modules.user.dao.UserMapperInterface
import br.meetingplace.server.modules.community.entitie.Community
import br.meetingplace.server.modules.community.entitie.CommunityMember
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.topic.entitie.Topic
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.topics.TopicCreationDTO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

object TopicFactoryDAO {

    fun create(data: TopicCreationDTO, userMapper: UserMapperInterface, communityMapper: CommunityDAOInterface): Status {
        return try {
            val user = transaction { User.select { User.id eq data.userID }.map { userMapper.mapUser(it) }.firstOrNull() }
            when (data.communityID.isNullOrBlank()) {
                true -> {
                    return if (user != null) {
                        transaction {
                            Topic.insert {
                                it[id] = UUID.randomUUID().toString()
                                it[header] = data.header
                                it[body] = data.body
                                it[imageURL] = data.imageURL
                                it[approved] = true
                                it[footer] = user.email
                                it[creatorID] = user.id
                                it[mainTopicID] = null
                                it[communityID] = null
                                it[creationDate] = DateTime.now()
                            }
                        }
                        Status(statusCode = 200, StatusMessages.OK)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
                false -> {
                    val member = transaction { CommunityMember.select { CommunityMember.userID eq data.userID }.map { communityMapper.mapCommunityMembersDTO(it) }.firstOrNull() }
                    return if (transaction { Community.select { Community.id eq data.communityID }.firstOrNull() }!= null
                        && user != null && member != null) {
                        transaction {
                            Topic.insert {
                                it[id] = UUID.randomUUID().toString()
                                it[header] = data.header
                                it[body] = data.body
                                it[imageURL] = data.imageURL
                                it[approved] = member.admin
                                it[footer] = user.email
                                it[creatorID] = user.id
                                it[mainTopicID] = null
                                it[communityID] = member.communityID
                                it[creationDate] = DateTime.now()
                            }
                        }
                        Status(statusCode = 200, StatusMessages.OK)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
            }
        } catch (e: Exception) {
            println(e.message)
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    fun createComment(data: TopicCreationDTO, userMapper: UserMapperInterface, communityMapper: CommunityDAOInterface): Status {
        return try {
            val user = transaction { User.select { User.id eq data.userID }.map { userMapper.mapUser(it) }.firstOrNull() }
            when (data.communityID.isNullOrBlank()) {
                true -> {
                    return if (user != null && !data.mainTopicID.isNullOrBlank() && transaction { !Topic.select {(Topic.id eq data.mainTopicID) and (Topic.communityID eq null)}.empty() }) {
                        transaction {
                            Topic.insert {
                                it[id] = UUID.randomUUID().toString()
                                it[header] = data.header
                                it[body] = data.body
                                it[imageURL] = data.imageURL
                                it[approved] = true
                                it[footer] = user.email
                                it[creatorID] = user.id
                                it[mainTopicID] = data.mainTopicID
                                it[communityID] = null
                                it[creationDate] =  DateTime.now()
                            }
                        }
                        Status(statusCode = 200, StatusMessages.OK)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
                false -> {
                    val member = transaction { CommunityMember.select { CommunityMember.userID eq data.userID }.map { communityMapper.mapCommunityMembersDTO(it) }.firstOrNull() }
                    return if (!data.mainTopicID.isNullOrBlank() && transaction { !Topic.select { (Topic.id eq data.mainTopicID) and (Topic.approved eq true)}.empty() } && user != null && member != null) {
                        transaction {
                            Topic.insert {
                                it[id] = UUID.randomUUID().toString()
                                it[header] = data.header
                                it[body] = data.body
                                it[imageURL] = data.imageURL
                                it[approved] = true
                                it[footer] = user.email
                                it[creatorID] = user.id
                                it[mainTopicID] = data.mainTopicID
                                it[communityID] = member.communityID
                                it[creationDate] =  DateTime.now()
                            }
                        }
                        Status(statusCode = 200, StatusMessages.OK)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
            }
        } catch (e: Exception) {
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}
