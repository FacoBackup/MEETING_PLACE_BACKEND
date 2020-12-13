package br.meetingplace.server.modules.message.routes

import br.meetingplace.server.methods.AES
import br.meetingplace.server.modules.group.dao.GroupDAO
import br.meetingplace.server.modules.group.dao.member.GroupMemberDAO
import br.meetingplace.server.modules.message.dao.MessageDAO
import br.meetingplace.server.modules.message.dao.opinions.MessageOpinionDAO
import br.meetingplace.server.modules.message.dto.requests.RequestConversation
import br.meetingplace.server.modules.message.dto.requests.RequestConversationMessage
import br.meetingplace.server.modules.message.dto.requests.RequestMessage
import br.meetingplace.server.modules.message.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.message.services.delete.MessageDeleteService
import br.meetingplace.server.modules.message.services.factory.MessageFactoryService
import br.meetingplace.server.modules.message.services.opinion.MessageOpinionService
import br.meetingplace.server.modules.message.services.quote.MessageQuoteService
import br.meetingplace.server.modules.message.services.read.ChatReadService
import br.meetingplace.server.modules.message.services.share.MessageShareService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.messageRouter() {

    route("/api") {

        post<RequestConversation>("/get/conversation") {
            val log = call.log
            println("---------------------------------------------------------------------------------------")
            println(it)
            if(log != null){
                val chats = ChatReadService.readConversation(requester = log.userID, subjectID = it.subjectID, isGroup = it.isGroup, decryption = AES, messageDAO = MessageDAO)
                println(chats)
                call.respond(chats)
            }

            else call.respond(HttpStatusCode.Unauthorized)
        }

        post<RequestMessageCreation>(ChatPaths.MESSAGE) {
            val log = call.log
            println(it)
            if(log != null)
                call.respond(MessageFactoryService.createMessage(requester = log.userID, it, GroupMemberDAO, UserDAO, GroupDAO, MessageDAO, AES))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        delete(ChatPaths.MESSAGE) {
            val data = call.receive<RequestMessage>()
            val log = call.log
            if(log != null)
                call.respond(MessageDeleteService.deleteMessage(requester = log.userID,data, MessageDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        put(ChatPaths.LIKE) {
            val data = call.receive<RequestMessage>()
            val log = call.log
            if(log != null)
                call.respond(MessageOpinionService.likeMessage(requester = log.userID,data, UserDAO, MessageDAO, MessageOpinionDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        put(ChatPaths.DISLIKE) {
            val data = call.receive<RequestMessage>()
            val log = call.log
            if(log != null)
                call.respond(MessageOpinionService.dislikeMessage(requester = log.userID,data, UserDAO, MessageDAO, MessageOpinionDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        post<RequestConversationMessage>(ChatPaths.QUOTE) {
            call.respond(MessageQuoteService.quoteMessage(it))
        }
        patch(ChatPaths.SHARE) {
            val data = call.receive<RequestConversationMessage>()
            call.respond(MessageShareService.shareMessage(data))
        }
    }
}