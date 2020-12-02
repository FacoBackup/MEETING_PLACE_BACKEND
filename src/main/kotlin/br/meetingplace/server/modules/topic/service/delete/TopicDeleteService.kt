package br.meetingplace.server.modules.topic.service.delete

import br.meetingplace.server.modules.topic.dao.TI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.topic.entitie.Topic
import br.meetingplace.server.request.dto.topics.TopicDTO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object TopicDeleteService{

    fun deleteTopic(data: TopicDTO, topicDAO: TI): Status {
        return try {
            val topic = topicDAO.read(topicID = data.topicID)
            if(topic != null && topic.creatorID == data.userID)
                topicDAO.delete(data.topicID)
            else
                Status(401, StatusMessages.UNAUTHORIZED)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}