package br.meetingplace.server.modules.conversation.services.conversation.delete

import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversation
import io.ktor.http.*

object ConversationDeleteService{
    suspend fun delete(requester: Long, data: RequestConversation, conversationMemberDAO: CMI, conversationDAO: CI): HttpStatusCode {
        return try{
            val member = data.conversationID?.let { conversationMemberDAO.read(userID = requester, conversationID = it) }
            if(member != null && member.admin)
                conversationDAO.delete(data.conversationID)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}