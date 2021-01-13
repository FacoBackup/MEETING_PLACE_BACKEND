package br.meetingplace.server.modules.conversation.services.conversation.delete

import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversation
import io.ktor.http.*

object ConversationDeleteService{
    suspend fun delete(requester: String, data: RequestConversation, conversationMemberDAO: CMI, groupDAO: CI): HttpStatusCode {
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