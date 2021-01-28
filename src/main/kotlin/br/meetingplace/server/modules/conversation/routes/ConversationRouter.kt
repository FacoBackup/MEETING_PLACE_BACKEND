package br.meetingplace.server.modules.conversation.routes

import br.meetingplace.server.modules.conversation.dao.conversation.ConversationDAO
import br.meetingplace.server.modules.conversation.dao.conversation.member.ConversationMemberDAO
import br.meetingplace.server.modules.conversation.dao.conversation.owners.ConversationOwnersDAO
import br.meetingplace.server.modules.conversation.dao.messages.status.MessageStatusDAO
import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversation
import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversationNewest
import br.meetingplace.server.modules.conversation.services.conversation.factory.ConversationFactoryService
import br.meetingplace.server.modules.conversation.services.conversation.read.ConversationReadService
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

        get("/conversation/all"){
            val log = call.log
            if(log != null)
                call.respond(
                    ConversationReadService.readConversations(
                    log.userID,
                    conversationMemberDAO = ConversationMemberDAO,
                    conversationDAO = ConversationDAO,
                    userDAO = UserDAO,
                    conversationOwnerDAO = ConversationOwnersDAO,
                    messageStatusDAO = MessageStatusDAO
                ))
            else call.respond(HttpStatusCode.Unauthorized)
        }

        patch("/conversation/newest"){
            val data = call.receive<RequestConversationNewest>()
            val log = call.log
            if(log != null)
                call.respond(ConversationReadService.readNewestConversations(
                    requester = log.userID,
                    minID = data.minID,
                    conversationMemberDAO = ConversationMemberDAO,
                    conversationDAO = ConversationDAO,
                    userDAO = UserDAO,
                    conversationOwnerDAO = ConversationOwnersDAO,
                    messageStatusDAO = MessageStatusDAO
                ))
            else call.respond(HttpStatusCode.Unauthorized)
        }

        get("/conversation/update/info"){
            val log = call.log
            if(log != null)
                call.respond(ConversationReadService.updateConversationInfo(
                    requester = log.userID,
                   conversationID = 123,
                    conversationMemberDAO = ConversationMemberDAO,
                    conversationDAO = ConversationDAO,
                    userDAO = UserDAO,
                    conversationOwnerDAO = ConversationOwnersDAO,
                    messageStatusDAO = MessageStatusDAO))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch ("/conversation/search"){
            val data = call.receive<RequestConversation>()
            val log = call.log
            if(log != null && data.conversationName != null)
                call.respond(ConversationDAO.readByName(input = data.conversationName, log.userID))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch("/conversation/by/owner"){
            val data = call.receive<RequestUser>()
            val log = call.log
            if(log != null  && data.userID != null){
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