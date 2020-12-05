package br.meetingplace.server.modules.topic.services.factory

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.topic.dao.TI
import br.meetingplace.server.modules.topic.dto.requests.RequestTopicCreation
import io.ktor.http.*

object TopicFactoryService {

    fun create(data: RequestTopicCreation, topicDAO: TI, userDAO: UI, communityMemberDAO: CMI): HttpStatusCode {
        return try {
            val user = userDAO.read(data.userID)
            when (data.communityID.isNullOrBlank()) {
                true -> {
                    return if (user != null && data.mainTopicID.isNullOrBlank()) {
                        topicDAO.create(data,true, user.name)
                    } else HttpStatusCode.FailedDependency
                }
                false -> {
                    val member = communityMemberDAO.read(data.communityID, userID = data.userID)
                    return if (user != null && member != null && data.mainTopicID.isNullOrBlank()) {
                        topicDAO.create(data,
                            approved = member.role == MemberType.DIRECTOR.toString() || member.role == MemberType.LEADER.toString(),
                            userName = user.name
                        )
                    } else HttpStatusCode.FailedDependency
                }
            }
        } catch (e: Exception) {
            HttpStatusCode.InternalServerError
        }
    }

    fun createComment(data: RequestTopicCreation, topicDAO: TI, userDAO: UI, communityMemberDAO: CMI): HttpStatusCode {
        return try {
            val user = userDAO.read(data.userID)
            when (data.communityID.isNullOrBlank()) {
                true -> {
                    return if (user != null && !data.mainTopicID.isNullOrBlank() && topicDAO.check(data.mainTopicID)  == HttpStatusCode.Found) {
                        topicDAO.create(data, approved = true, userName = user.name)
                    } else HttpStatusCode.FailedDependency
                }
                false -> {
                    val member = communityMemberDAO.read(data.communityID, userID = data.userID)
                    val mainTopic = data.mainTopicID?.let { topicDAO.read(it) }
                    return if (mainTopic  != null && mainTopic.approved  && user != null && member != null) {
                        topicDAO.create(data,
                            approved = member.role == MemberType.DIRECTOR.toString() || member.role == MemberType.LEADER.toString(),
                            userName = user.name
                        )
                    } else HttpStatusCode.FailedDependency
                }
            }
        } catch (e: Exception) {
            HttpStatusCode.InternalServerError
        }
    }
}
