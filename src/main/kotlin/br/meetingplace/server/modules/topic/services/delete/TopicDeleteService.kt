package br.meetingplace.server.modules.topic.services.delete

import br.meetingplace.server.modules.topic.dao.TI
import br.meetingplace.server.modules.topic.dto.requests.RequestTopic
import io.ktor.http.*

object TopicDeleteService{

    fun deleteTopic(requester: String,data: RequestTopic, topicDAO: TI): HttpStatusCode {
        return try {
            val topic = topicDAO.read(topicID = data.topicID)
            if(topic != null && topic.creatorID == requester)
                topicDAO.delete(data.topicID)
            else
                HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}