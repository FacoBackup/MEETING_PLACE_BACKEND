package br.meetingplace.server.modules.topic.services.delete

import br.meetingplace.server.modules.topic.dao.TI
import br.meetingplace.server.modules.topic.dto.requests.RequestTopic

object TopicDeleteService{

    fun deleteTopic(data: RequestTopic, topicDAO: TI): Status {
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