package br.meetingplace.server.modules.conversation.services.message.opinion

import br.meetingplace.server.modules.conversation.dao.member.CMI
import br.meetingplace.server.modules.conversation.dao.messages.MessageDAO
import br.meetingplace.server.modules.conversation.dao.messages.opinions.MessageOpinionDAO
import br.meetingplace.server.modules.conversation.dto.requests.RequestMessage
import br.meetingplace.server.modules.user.dao.user.UserDAO
import io.ktor.http.*

object MessageOpinionService {
    fun dislikeMessage(requester: String, data: RequestMessage, userDAO: UserDAO, messageDAO: MessageDAO, messageOpinionsDAO: MessageOpinionDAO, conversationMemberDAO: CMI): HttpStatusCode {
        return try {
            if(messageDAO.check(data.messageID) && userDAO.check(requester)  && conversationMemberDAO.check(conversationID = data.conversationID, userID = requester)){
                when(messageOpinionsDAO.read(data.messageID, userID = requester) == null){
                    true-> messageOpinionsDAO.create(data.messageID, userID = requester, liked = false)
                    false-> {
                        val opinion = messageOpinionsDAO.read(data.messageID, userID = requester)
                        if(opinion != null && !opinion.liked) messageOpinionsDAO.delete(data.messageID, userID = requester)
                        else messageOpinionsDAO.update(data.messageID, userID = requester, liked = false)
                    }
                }
            }
            else HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    fun likeMessage(requester: String, data: RequestMessage, userDAO: UserDAO, messageDAO: MessageDAO, messageOpinionsDAO: MessageOpinionDAO, conversationMemberDAO: CMI): HttpStatusCode {
        return try {
            if(messageDAO.check(data.messageID) && userDAO.check(requester)  && conversationMemberDAO.check(conversationID = data.conversationID, userID = requester)){
                when(messageOpinionsDAO.read(data.messageID, userID = requester) == null){
                    true-> messageOpinionsDAO.create(data.messageID, userID = requester, liked = true)
                    false-> {
                        val opinion = messageOpinionsDAO.read(data.messageID, userID = requester)
                        if(opinion != null && opinion.liked) messageOpinionsDAO.delete(data.messageID, userID = requester)
                        else messageOpinionsDAO.update(data.messageID, userID = requester, liked = true)
                    }
                }
            }
            else HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}