package br.meetingplace.server.modules.topic.dao.factory

import br.meetingplace.server.db.mapper.community.CommunityMapperInterface
import br.meetingplace.server.db.mapper.user.UserMapperInterface
import br.meetingplace.server.modules.communityTODOTRANSACTIONS.db.Community
import br.meetingplace.server.modules.communityTODOTRANSACTIONS.db.CommunityMember
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.topics.data.TopicCreationData
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*

object TopicFactoryDAO {

    fun create(data: TopicCreationData, userMapper: UserMapperInterface, communityMapper: CommunityMapperInterface): Status {
        return try {
            val user = transaction { User.select { User.id eq data.userID }.map { userMapper.mapUser(it) } }.firstOrNull()
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
                                it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                            }
                        }
                        Status(statusCode = 200, StatusMessages.OK)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
                false -> {
                    val member = transaction { CommunityMember.select { CommunityMember.userID eq data.userID }.map { communityMapper.mapCommunityMembersDTO(it) } }.firstOrNull()
                    return if (transaction { Community.select { Community.id eq data.communityID } }.firstOrNull() != null
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
                                it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
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
    fun createComment(data: TopicCreationData, userMapper: UserMapperInterface, memberMapper: CommunityMapperInterface): Status {
        return try {
            val user = transaction { User.select { User.id eq data.userID }.map { userMapper.mapUser(it) } }.firstOrNull()
            when (data.communityID.isNullOrBlank()) {
                true -> {
                    return if (user != null && !data.mainTopicID.isNullOrBlank() && transaction { Topic.select {(Topic.id eq data.mainTopicID) and (Topic.communityID eq null)} }.firstOrNull() != null) {
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
                                it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                            }
                        }
                        Status(statusCode = 200, StatusMessages.OK)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
                false -> {
                    val member = CommunityMember.select { CommunityMember.userID eq data.userID }.map { memberMapper.mapCommunityMembersDTO(it) }.firstOrNull()
                    return if (!data.mainTopicID.isNullOrBlank() && transaction { Topic.select { (Topic.id eq data.mainTopicID) and (Topic.approved eq true)} }.firstOrNull() != null && user != null && member != null) {
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
                                it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
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
