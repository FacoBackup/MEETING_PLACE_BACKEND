package br.meetingplace.server.modules.message.services.opinion

import br.meetingplace.server.modules.message.dao.MessageDAO
import br.meetingplace.server.modules.message.dao.opinions.MessageOpinionDAO
import br.meetingplace.server.modules.message.dto.requests.RequestMessage
import br.meetingplace.server.modules.user.dao.user.UserDAO

object MessageOpinionService {
    fun dislikeMessage(data: RequestMessage, userDAO: UserDAO, messageDAO: MessageDAO, messageOpinionsDAO: MessageOpinionDAO): Status {
        return try {
            if(messageDAO.read(data.messageID) != null && userDAO.read(data.userID) != null){
                when(messageOpinionsDAO.read(data.messageID, userID = data.userID) == null){
                    true-> messageOpinionsDAO.create(data.messageID, userID = data.userID, liked = false)
                    false-> {
                        val opinion = messageOpinionsDAO.read(data.messageID, userID = data.userID)
                        if(opinion != null && !opinion.liked) messageOpinionsDAO.delete(data.messageID, userID = data.userID)
                        else messageOpinionsDAO.update(data.messageID, userID = data.userID, liked = false)
                    }
                }
            }
            else Status(400, StatusMessages.NOT_FOUND)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun likeMessage(data: RequestMessage, userDAO: UserDAO, messageDAO: MessageDAO, messageOpinionsDAO: MessageOpinionDAO): Status {
        return try {
            if(messageDAO.read(data.messageID) != null && userDAO.read(data.userID) != null){
                when(messageOpinionsDAO.read(data.messageID, userID = data.userID) == null){
                    true-> messageOpinionsDAO.create(data.messageID, userID = data.userID, liked = true)
                    false-> {
                        val opinion = messageOpinionsDAO.read(data.messageID, userID = data.userID)
                        if(opinion != null && opinion.liked) messageOpinionsDAO.delete(data.messageID, userID = data.userID)
                        else messageOpinionsDAO.update(data.messageID, userID = data.userID, liked = true)
                    }
                }
            }
            else Status(400, StatusMessages.NOT_FOUND)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}