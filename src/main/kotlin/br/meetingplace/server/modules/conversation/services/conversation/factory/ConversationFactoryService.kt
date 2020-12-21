package br.meetingplace.server.modules.conversation.services.conversation.factory

import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversationCreation
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*
import java.util.*

object ConversationFactoryService {

    fun create(requester: String, conversationMemberDAO: CMI, data: RequestConversationCreation, conversationDAO: CI, userDAO: UI) : HttpStatusCode {
        return try {
            if (userDAO.check(requester)){
                val id = UUID.randomUUID().toString()
                conversationDAO.create(data, id = id)
                conversationMemberDAO.create(userID= requester, conversationID = id, admin = true)
            }
            else HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }

}