package br.meetingplace.server.modules.topic.services.opinion

import br.meetingplace.server.modules.topic.dao.TI
import br.meetingplace.server.modules.topic.dao.opinion.TOI
import br.meetingplace.server.modules.topic.dto.response.TopicOpinionDTO
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.topic.dto.requests.RequestTopic

object TopicOpinionService{

    fun dislike(data: RequestTopic, userDAO: UI, topicDAO: TI, topicOpinionDAO: TOI): Status {
        return try {
            return if (userDAO.read(data.userID) != null && topicDAO.read(data.topicID) != null)
                 when (checkLikeDislike(topicOpinion = topicOpinionDAO.read(data.topicID, userID = data.userID))) {
                    0 -> topicOpinionDAO.update(topicID = data.topicID, userID = data.userID, false)// like to dislike
                    1 -> topicOpinionDAO.delete(topicID = data.topicID, userID = data.userID)
                    2 -> topicOpinionDAO.create(topicID = data.topicID, userID = data.userID, false)
                    else -> Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                 }
            else Status(statusCode = 404, StatusMessages.NOT_FOUND)
        }catch (normal: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun like(data: RequestTopic, userDAO: UI, topicDAO: TI, topicOpinionDAO: TOI): Status {
        return try {
            return if (userDAO.read(data.userID) != null && topicDAO.read(data.topicID) != null)
                when (checkLikeDislike(topicOpinion = topicOpinionDAO.read(data.topicID, userID = data.userID))) {
                    0 -> topicOpinionDAO.delete(topicID = data.topicID, userID = data.userID)
                    1 -> topicOpinionDAO.update(topicID = data.topicID, userID = data.userID, true)// like to dislike
                    2 -> topicOpinionDAO.create(topicID = data.topicID, userID = data.userID, true)
                    else -> Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                }
            else Status(statusCode = 404, StatusMessages.NOT_FOUND)
        }catch (normal: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    private fun checkLikeDislike(topicOpinion: TopicOpinionDTO?): Int {

        return when{
            topicOpinion == null -> 2 // 2 hasn't DISLIKED or liked yet
            topicOpinion.liked -> 0
            !topicOpinion.liked -> 1 // 1 ALREADY DISLIKED
            else -> 2
        }
    }
}