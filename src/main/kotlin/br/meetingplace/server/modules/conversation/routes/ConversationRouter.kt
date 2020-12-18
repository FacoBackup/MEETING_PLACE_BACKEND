package br.meetingplace.server.modules.conversation.routes

import br.meetingplace.server.methods.AES
import br.meetingplace.server.modules.conversation.dao.ConversationDAO
import br.meetingplace.server.modules.conversation.dao.member.ConversationMemberDAO
import br.meetingplace.server.modules.conversation.dto.requests.RequestConversation
import br.meetingplace.server.modules.conversation.dto.requests.RequestConversationCreation
import br.meetingplace.server.modules.conversation.dto.requests.RequestConversationMember
import br.meetingplace.server.modules.conversation.services.delete.ConversationDeleteService
import br.meetingplace.server.modules.conversation.services.factory.ConversationFactoryService
import br.meetingplace.server.modules.conversation.services.member.ConversationMemberService
import br.meetingplace.server.modules.conversation.services.message.read.MessageReadService
import br.meetingplace.server.modules.conversation.dao.messages.MessageDAO
import br.meetingplace.server.modules.conversation.dao.messages.opinions.MessageOpinionDAO
import br.meetingplace.server.modules.conversation.dao.owners.ConversationOwnersDAO
import br.meetingplace.server.modules.conversation.dto.requests.RequestConversationMessage
import br.meetingplace.server.modules.conversation.dto.requests.RequestMessage
import br.meetingplace.server.modules.conversation.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.conversation.services.message.delete.MessageDeleteService
import br.meetingplace.server.modules.conversation.services.message.factory.MessageFactoryService
import br.meetingplace.server.modules.conversation.services.message.opinion.MessageOpinionService
import br.meetingplace.server.modules.conversation.services.message.quote.MessageQuoteService
import br.meetingplace.server.modules.conversation.services.message.share.MessageShareService
import br.meetingplace.server.modules.conversation.services.read.ConversationReadService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.userConversationRouter() {
    route("/api") {
        get("/conversation/all"){
            val log = call.log
            if(log != null)
                call.respond(ConversationReadService.readConversation(log.userID, conversationMemberDAO = ConversationMemberDAO, conversationDAO = ConversationDAO, userDAO = UserDAO, conversationOwnerDAO = ConversationOwnersDAO))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        post<RequestUser>("/get/conversation/user") {
            val log = call.log
            if(log != null){
                val owners = ConversationOwnersDAO.read(userID = log.userID, secondUserID = it.userID)
                if(owners != null) {
                    val conversation = ConversationDAO.read(conversationID = owners.conversationID)
                    if (conversation != null)
                        call.respond(conversation)
                }
                else
                    call.respond(HttpStatusCode.NoContent)
            }
            else call.respond(HttpStatusCode.Unauthorized)
        }
        post<RequestUser>("/get/conversation/user/messages") {
            val log = call.log
            if(log != null){
                val chats = MessageReadService.readUserMessages(requester = log.userID,
                    userID = it.userID,
                    decryption = AES,
                    messageDAO = MessageDAO,
                    conversationOwnerDAO = ConversationOwnersDAO)

                call.respond(chats)
            }
            else call.respond(HttpStatusCode.Unauthorized)
        }


        post<RequestMessageCreation>("/message/user") {
            val log = call.log
            println(it)
            if(log != null)
                call.respond(MessageFactoryService.createUserMessage(requester = log.userID, it, conversationOwnerDAO = ConversationOwnersDAO, UserDAO, ConversationDAO, MessageDAO, AES))
            else call.respond(HttpStatusCode.Unauthorized)
        }

    }
}