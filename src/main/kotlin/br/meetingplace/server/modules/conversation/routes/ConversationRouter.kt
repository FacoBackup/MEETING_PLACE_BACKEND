package br.meetingplace.server.modules.conversation.routes

import br.meetingplace.server.modules.conversation.dao.conversation.ConversationDAO
import br.meetingplace.server.modules.conversation.dao.conversation.member.ConversationMemberDAO
import br.meetingplace.server.modules.conversation.dao.conversation.owners.ConversationOwnersDAO
import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversation
import br.meetingplace.server.modules.conversation.services.conversation.factory.ConversationFactoryService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.conversationRouter(){
    route("/api"){
        patch ("/conversation/search"){
            val data = call.receive<RequestConversation>()
            val log = call.log
            if(log != null)
                call.respond(ConversationDAO.readByName(input = data.conversationID, log.userID))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch("/conversation/by/owner"){
            val data = call.receive<RequestUser>()
            val log = call.log
            if(log != null){
                val result = ConversationOwnersDAO.read(userID = data.userID, log.userID)
                if(result != null)
                    call.respond(result)
                else
                    call.respond(HttpStatusCode.NoContent)
            }

            else call.respond(HttpStatusCode.Unauthorized)
        }
    }
}