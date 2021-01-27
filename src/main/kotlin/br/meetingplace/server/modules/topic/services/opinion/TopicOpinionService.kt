package br.meetingplace.server.modules.topic.services.opinion

import br.meetingplace.server.modules.topic.dao.topic.TI
import br.meetingplace.server.modules.topic.dao.opinion.TOI
import br.meetingplace.server.modules.topic.dto.requests.RequestTopic
import br.meetingplace.server.modules.topic.dto.response.TopicOpinionDTO
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*

object TopicOpinionService{

    suspend fun dislike(requester: Long, data: RequestTopic, userDAO: UI, topicDAO: TI, topicOpinionDAO: TOI): HttpStatusCode {
        return try {
            return if (userDAO.check(requester) && topicDAO.check(data.topicID))
                 when (checkLikeDislike(topicOpinion = topicOpinionDAO.read(data.topicID, userID = requester))) {
                    0 -> topicOpinionDAO.update(topicID = data.topicID, userID = requester, false)// like to dislike
                    1 -> topicOpinionDAO.delete(topicID = data.topicID, userID = requester)
                    2 -> topicOpinionDAO.create(topicID = data.topicID, userID = requester, false)
                    else -> HttpStatusCode.FailedDependency
                 }
            else HttpStatusCode.InternalServerError
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    suspend fun like(requester: Long, data: RequestTopic, userDAO: UI, topicDAO: TI, topicOpinionDAO: TOI): HttpStatusCode {
        return try {
            return if (userDAO.check(requester) && topicDAO.check(data.topicID))
                when (checkLikeDislike(topicOpinion = topicOpinionDAO.read(data.topicID, userID = requester))) {
                    0 -> topicOpinionDAO.delete(topicID = data.topicID, userID = requester)
                    1 -> topicOpinionDAO.update(topicID = data.topicID, userID = requester, true)// like to dislike
                    2 -> topicOpinionDAO.create(topicID = data.topicID, userID = requester, true)
                    else -> HttpStatusCode.FailedDependency
                }
            else HttpStatusCode.InternalServerError
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    private fun checkLikeDislike(topicOpinion: TopicOpinionDTO?): Int {

        return when{
            topicOpinion == null -> 2 // 2 hasn't DISLIKED or liked yet
            topicOpinion.liked -> 0
            !topicOpinion.liked -> 1 // 1 ALREADY DISLIKED
            else -> -1
        }
    }
}