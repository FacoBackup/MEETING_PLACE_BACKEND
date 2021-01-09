package br.meetingplace.server.modules.conversation.routes

import br.meetingplace.server.methods.AES
import br.meetingplace.server.modules.conversation.dao.conversation.ConversationDAO
import br.meetingplace.server.modules.conversation.dao.conversation.member.ConversationMemberDAO
import br.meetingplace.server.modules.conversation.services.message.read.MessageReadService
import br.meetingplace.server.modules.conversation.dao.messages.MessageDAO
import br.meetingplace.server.modules.conversation.dao.conversation.owners.ConversationOwnersDAO
import br.meetingplace.server.modules.conversation.dao.messages.status.MessageStatusDAO
import br.meetingplace.server.modules.conversation.dto.requests.messages.RequestMessageCreation
import br.meetingplace.server.modules.conversation.services.message.factory.MessageFactoryService
import br.meetingplace.server.modules.conversation.services.conversation.read.ConversationReadService
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
        patch("/get/conversationID"){
            val data = call.receive<RequestUser>()
            val log = call.log
            if(log != null){
                val conv = ConversationOwnersDAO.read(userID = data.userID, secondUserID = log.userID)
                if(conv != null)
                    call.respond(conv.conversationID)
                else
                    call.respond(HttpStatusCode.NoContent)
            }
            else call.respond(HttpStatusCode.Unauthorized)
        }
        get("/conversation/all"){
            val log = call.log
            if(log != null)
                call.respond(ConversationReadService.readConversations(
                    log.userID,
                    conversationMemberDAO = ConversationMemberDAO,
                    conversationDAO = ConversationDAO,
                    userDAO = UserDAO,
                    conversationOwnerDAO = ConversationOwnersDAO,
                    messageStatusDAO = MessageStatusDAO
                    ))
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
        post<RequestUser>("/get/all/user/messages") {
            val log = call.log
            if(log != null){
                val chats = MessageReadService.readUserAllMessages(
                    requester = log.userID,
                    userID = it.userID,
                    decryption = AES,
                    messageDAO = MessageDAO,
                    conversationOwnerDAO = ConversationOwnersDAO,
                    messageStatusDAO = MessageStatusDAO)

                call.respond(chats)
            }
            else call.respond(HttpStatusCode.Unauthorized)
        }
        post<RequestUser>("/get/new/user/messages") {
            val log = call.log
            if(log != null){
                val messages = MessageReadService.readNewUserMessages(
                    requester = log.userID,
                    userID = it.userID,
                    decryption = AES,
                    messageDAO = MessageDAO,
                    conversationOwnerDAO = ConversationOwnersDAO,
                    messageStatusDAO = MessageStatusDAO,
                    userDAO = UserDAO
                )
                call.respond(messages)
            }
            else call.respond(HttpStatusCode.Unauthorized)
        }

        post<RequestMessageCreation>("/message/user") {
            val log = call.log

            if(log != null)
                call.respond(MessageFactoryService.createUserMessage(requester = log.userID,
                    it,
                    conversationOwnerDAO = ConversationOwnersDAO,
                    userDAO = UserDAO,
                    conversationDAO = ConversationDAO,
                    messageDAO = MessageDAO,
                    encryption = AES,
                    messageStatusDAO = MessageStatusDAO
                    ))
            else call.respond(HttpStatusCode.Unauthorized)
        }

    }
}