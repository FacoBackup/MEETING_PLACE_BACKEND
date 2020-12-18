package br.meetingplace.server.modules.conversation.routes

import br.meetingplace.server.methods.AES
import br.meetingplace.server.modules.conversation.dao.ConversationDAO
import br.meetingplace.server.modules.conversation.dao.member.ConversationMemberDAO
import br.meetingplace.server.modules.conversation.dao.messages.MessageDAO
import br.meetingplace.server.modules.conversation.dto.requests.RequestConversation
import br.meetingplace.server.modules.conversation.dto.requests.RequestConversationCreation
import br.meetingplace.server.modules.conversation.dto.requests.RequestConversationMember
import br.meetingplace.server.modules.conversation.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.conversation.services.delete.ConversationDeleteService
import br.meetingplace.server.modules.conversation.services.factory.ConversationFactoryService
import br.meetingplace.server.modules.conversation.services.member.ConversationMemberService
import br.meetingplace.server.modules.conversation.services.message.factory.MessageFactoryService
import br.meetingplace.server.modules.conversation.services.message.read.MessageReadService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.groupConversationRouter(){
    route("/api"){
        post<RequestConversationCreation>("/group") {
            val log = call.log
            if(log != null)
                call.respond(
                    ConversationFactoryService.create(
                    requester = log.userID,
                    data = it,
                    conversationDAO = ConversationDAO,
                    userDAO = UserDAO,
                    conversationMemberDAO = ConversationMemberDAO
                    ))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        delete("/group") {
            val data = call.receive<RequestConversation>()
            val log = call.log
            if(log != null)
                call.respond(
                    ConversationDeleteService.delete(
                    requester = log.userID,
                    data,
                    ConversationMemberDAO, ConversationDAO
                    ))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        patch("/group/member") {
            val data = call.receive<RequestConversationMember>()
            val log = call.log
            if(log != null)
                call.respond(
                    ConversationMemberService.addMember(
                    requester = log.userID,
                    data =data,
                    conversationMemberDAO = ConversationMemberDAO,
                    conversationDAO = ConversationDAO
                ))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        delete("/group/member") {
            val data = call.receive<RequestConversationMember>()
            val log = call.log
            if(log != null)
                call.respond(
                    ConversationMemberService.removeMember(requester = log.userID,
                    data,
                    conversationMemberDAO = ConversationMemberDAO,
                    conversationDAO = ConversationDAO
                ))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        post<RequestConversation>("/get/conversation/group") {
            val log = call.log
            if(log != null){
                val conversation = ConversationDAO.read(conversationID = it.conversationID)
                if (conversation != null)
                    call.respond(conversation)
                else
                    call.respond(HttpStatusCode.NoContent)
            }
            else call.respond(HttpStatusCode.Unauthorized)
        }
        post<RequestConversation>("/get/conversation/group/messages") {
            val log = call.log
            if(log != null){
                val chats = MessageReadService.readGroupMessages(requester = log.userID,
                    conversationID = it.conversationID,
                    decryption = AES,
                    messageDAO = MessageDAO,
                    conversationMemberDAO = ConversationMemberDAO)

                call.respond(chats)
            }
            else call.respond(HttpStatusCode.Unauthorized)
        }
        post<RequestMessageCreation>("/message/group") {
            val log = call.log
            println(it)
            if(log != null)
                call.respond(MessageFactoryService.createGroupMessage(requester = log.userID, it, ConversationMemberDAO, UserDAO, ConversationDAO, MessageDAO, AES))
            else call.respond(HttpStatusCode.Unauthorized)
        }

    }
}