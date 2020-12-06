package br.meetingplace.server.modules.message.services.opinion

import br.meetingplace.server.modules.message.dao.MessageDAO
import br.meetingplace.server.modules.message.dao.opinions.MessageOpinionDAO
import br.meetingplace.server.modules.message.dto.requests.RequestMessage
import br.meetingplace.server.modules.user.dao.user.UserDAO
import io.ktor.http.*

object MessageOpinionService {
    fun dislikeMessage(data: RequestMessage, userDAO: UserDAO, messageDAO: MessageDAO, messageOpinionsDAO: MessageOpinionDAO): HttpStatusCode {
        return try {
            if(messageDAO.check(data.messageID) && userDAO.check(data.userID)){
                when(messageOpinionsDAO.read(data.messageID, userID = data.userID) == null){
                    true-> messageOpinionsDAO.create(data.messageID, userID = data.userID, liked = false)
                    false-> {
                        val opinion = messageOpinionsDAO.read(data.messageID, userID = data.userID)
                        if(opinion != null && !opinion.liked) messageOpinionsDAO.delete(data.messageID, userID = data.userID)
                        else messageOpinionsDAO.update(data.messageID, userID = data.userID, liked = false)
                    }
                }
            }
            else HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    fun likeMessage(data: RequestMessage, userDAO: UserDAO, messageDAO: MessageDAO, messageOpinionsDAO: MessageOpinionDAO): HttpStatusCode {
        return try {
            if(messageDAO.check(data.messageID) && userDAO.check(data.userID) ){
                when(messageOpinionsDAO.read(data.messageID, userID = data.userID) == null){
                    true-> messageOpinionsDAO.create(data.messageID, userID = data.userID, liked = true)
                    false-> {
                        val opinion = messageOpinionsDAO.read(data.messageID, userID = data.userID)
                        if(opinion != null && opinion.liked) messageOpinionsDAO.delete(data.messageID, userID = data.userID)
                        else messageOpinionsDAO.update(data.messageID, userID = data.userID, liked = true)
                    }
                }
            }
            else HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}