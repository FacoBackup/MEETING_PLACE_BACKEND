package br.meetingplace.server.modules.topic.service.factory

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.user.dao.UI
import br.meetingplace.server.modules.community.entitie.Community
import br.meetingplace.server.modules.community.entitie.CommunityMember
import br.meetingplace.server.modules.community.type.MemberType
import br.meetingplace.server.modules.topic.dao.TI
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

object TopicFactory {

    fun create(data: TopicCreationDTO, topicDAO: TI,userDAO: UI, communityMemberDAO: CMI): Status {
        return try {
            val user = userDAO.read(data.userID)
            when (data.communityID.isNullOrBlank()) {
                true -> {
                    return if (user != null && data.mainTopicID.isNullOrBlank()) {
                        topicDAO.create(data,true, user.name)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
                false -> {
                    val member = communityMemberDAO.read(data.communityID, userID = data.userID)
                    return if (user != null && member != null && data.mainTopicID.isNullOrBlank()) {
                        topicDAO.create(data,
                            approved = member.role == MemberType.DIRECTOR.toString() || member.role == MemberType.LEADER.toString(),
                            userName = user.name
                        )
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
            }
        } catch (e: Exception) {
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    fun createComment(data: TopicCreationDTO, topicDAO: TI,userDAO: UI, communityMemberDAO: CMI): Status {
        return try {
            val user = userDAO.read(data.userID)
            when (data.communityID.isNullOrBlank()) {
                true -> {
                    return if (user != null && !data.mainTopicID.isNullOrBlank() && topicDAO.read(data.mainTopicID)  != null) {
                        topicDAO.create(data, approved = true, userName = user.name)
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
                false -> {
                    val member = communityMemberDAO.read(data.communityID, userID = data.userID)
                    val mainTopic = data.mainTopicID?.let { topicDAO.read(it) }
                    return if (mainTopic  != null && mainTopic.approved  && user != null && member != null) {
                        topicDAO.create(data,
                            approved = member.role == MemberType.DIRECTOR.toString() || member.role == MemberType.LEADER.toString(),
                            userName = user.name
                        )
                    } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
            }
        } catch (e: Exception) {
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}
