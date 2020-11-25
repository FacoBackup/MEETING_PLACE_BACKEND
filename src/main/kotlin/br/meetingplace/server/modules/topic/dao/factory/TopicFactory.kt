package br.meetingplace.server.modules.topic.dao.factory

import br.meetingplace.server.db.mapper.community.CommunityMapperInterface
import br.meetingplace.server.db.mapper.topic.TopicMapperInterface
import br.meetingplace.server.db.mapper.user.UserMapperInterface
import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.modules.community.db.CommunityMember
import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.modules.global.http.status.StatusMessages
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.topics.data.TopicCreationData
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import java.text.DateFormat
import java.util.*

object TopicFactory {

    fun create(data: TopicCreationData, userMapper: UserMapperInterface, topicMapper: TopicMapperInterface, memberMapper: CommunityMapperInterface): Status {
        return try {
            val user = User.select { User.id eq data.userID }.map { userMapper.mapUser(it) }.firstOrNull()
            when (data.communityID.isNullOrBlank()) {
                true -> {
                    return if (user != null) {
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
                        Status(statusCode = 200, StatusMessages.OK)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
                false -> {
                    val member = CommunityMember.select { CommunityMember.userID eq data.userID }.map { memberMapper.mapCommunityMembersDTO(it) }.firstOrNull()
                    return if (!Community.select { Community.id eq data.communityID }.empty() && user != null && member != null) {
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
                        Status(statusCode = 200, StatusMessages.OK)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
            }
        } catch (e: Exception) {
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    fun createComment(data: TopicCreationData, userMapper: UserMapperInterface, topicMapper: TopicMapperInterface, memberMapper: CommunityMapperInterface): Status {
        return try {
            val user = User.select { User.id eq data.userID }.map { userMapper.mapUser(it) }.firstOrNull()
            when (data.communityID.isNullOrBlank()) {
                true -> {
                    return if (user != null && !data.mainTopicID.isNullOrBlank() && !Topic.select {(Topic.id eq data.mainTopicID) and (Topic.communityID eq null)}.empty()) {
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
                        Status(statusCode = 200, StatusMessages.OK)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
                false -> {
                    val member = CommunityMember.select { CommunityMember.userID eq data.userID }.map { memberMapper.mapCommunityMembersDTO(it) }.firstOrNull()
                    return if (!data.mainTopicID.isNullOrBlank() && !Topic.select { (Topic.id eq data.mainTopicID) and (Topic.approved eq true)}.empty() && user != null && member != null) {
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
                        Status(statusCode = 200, StatusMessages.OK)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
            }
        } catch (e: Exception) {
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}
