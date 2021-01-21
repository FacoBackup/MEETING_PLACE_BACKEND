package br.meetingplace.server.modules.conversation.services.conversation.factory

import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversationCreation
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*
import java.util.*

object ConversationFactoryService {

    suspend fun create(requester: Long, conversationMemberDAO: CMI, data: RequestConversationCreation, conversationDAO: CI, userDAO: UI) : HttpStatusCode {
        return try {
            if (userDAO.check(requester)){
                val id = conversationDAO.create(data)
                if(id != null)
                    conversationMemberDAO.create(userID= requester, conversationID = id, admin = true)
                else
                    HttpStatusCode.InternalServerError
            }
            else HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }

}