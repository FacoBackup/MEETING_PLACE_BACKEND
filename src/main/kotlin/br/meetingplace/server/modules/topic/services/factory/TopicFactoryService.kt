package br.meetingplace.server.modules.topic.services.factory

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.user.dao.UI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.topic.dao.TI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.topic.dto.requests.RequestTopicCreation

object TopicFactoryService {

    fun create(data: RequestTopicCreation, topicDAO: TI, userDAO: UI, communityMemberDAO: CMI): Status {
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
    fun createComment(data: RequestTopicCreation, topicDAO: TI, userDAO: UI, communityMemberDAO: CMI): Status {
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
