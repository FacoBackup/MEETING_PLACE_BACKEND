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
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.conversationRouter() {
    route("/api") {
        get("/conversations"){
            val log = call.log
            if(log != null)
                call.respond(ConversationReadService.readConversation(log.userID, conversationMemberDAO = ConversationMemberDAO, conversationDAO = ConversationDAO, userDAO = UserDAO))
            else call.respond(HttpStatusCode.Unauthorized)
        }

        post<RequestConversationCreation>("/group") {
            val log = call.log
            if(log != null)
                call.respond(ConversationFactoryService.create(
                    requester = log.userID,
                    data = it,
                    conversationDAO = ConversationDAO,
                    userDAO =UserDAO,
                    conversationMemberDAO = ConversationMemberDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        delete("/group") {
            val data = call.receive<RequestConversation>()
            val log = call.log
            if(log != null)
                call.respond(ConversationDeleteService.delete(
                    requester = log.userID,
                    data,
                    ConversationMemberDAO,ConversationDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        patch("/group/member") {
            val data = call.receive<RequestConversationMember>()
            val log = call.log
            if(log != null)
                call.respond(ConversationMemberService.addMember(
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
                call.respond(ConversationMemberService.removeMember(requester = log.userID,
                    data,
                    conversationMemberDAO = ConversationMemberDAO,
                    conversationDAO = ConversationDAO
                ))
            else call.respond(HttpStatusCode.Unauthorized)

        }

        post<RequestConversation>("/get/conversation") {
            val log = call.log
            if(log != null){
                val chats = MessageReadService.readConversationMessages(requester = log.userID,
                    conversationID = it.conversationID,
                    decryption = AES,
                    messageDAO = MessageDAO,
                    conversationMemberDAO = ConversationMemberDAO)

                call.respond(chats)
            }

            else call.respond(HttpStatusCode.Unauthorized)
        }

        post<RequestMessageCreation>("/message") {
            val log = call.log
            println(it)
            if(log != null)
                call.respond(MessageFactoryService.createMessage(requester = log.userID, it, ConversationMemberDAO, UserDAO, ConversationDAO, MessageDAO, AES))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        delete("/message") {
            val data = call.receive<RequestMessage>()
            val log = call.log
            if(log != null)
                call.respond(MessageDeleteService.deleteMessage(requester = log.userID,data, MessageDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        put("/message/like") {
            val data = call.receive<RequestMessage>()
            val log = call.log
            if(log != null)
                call.respond(
                    MessageOpinionService.likeMessage(
                    requester = log.userID,
                    data,
                    UserDAO,
                    MessageDAO,
                    MessageOpinionDAO,
                    conversationMemberDAO = ConversationMemberDAO
                ))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        put("/message/dislike") {
            val data = call.receive<RequestMessage>()
            val log = call.log
            if(log != null)
                call.respond(
                    MessageOpinionService.dislikeMessage(requester = log.userID,
                    data,
                    UserDAO,
                    MessageDAO,
                    messageOpinionsDAO = MessageOpinionDAO,
                    conversationMemberDAO = ConversationMemberDAO
                ))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        post<RequestConversationMessage>("/message/quote") {

            val log = call.log
            if(log != null)
                call.respond(MessageQuoteService.quoteMessage(it))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        post<RequestConversationMessage>("/message/share") {
            val log = call.log
            if(log != null)
                call.respond(MessageShareService.shareMessage(it))
            else call.respond(HttpStatusCode.Unauthorized)

        }
    }
}