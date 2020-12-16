package br.meetingplace.server.modules.conversation.services.delete

import br.meetingplace.server.modules.conversation.dao.CI
import br.meetingplace.server.modules.conversation.dao.member.CMI
import br.meetingplace.server.modules.conversation.dto.requests.RequestConversation
import io.ktor.http.*

object ConversationDeleteService{
    fun delete(requester: String, data: RequestConversation, conversationMemberDAO: CMI, groupDAO: CI): HttpStatusCode {
        return try{
            val member = conversationMemberDAO.read(userID = requester, conversationID = data.conversationID)
            if(member != null && member.admin)
                groupDAO.delete(data.conversationID)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}