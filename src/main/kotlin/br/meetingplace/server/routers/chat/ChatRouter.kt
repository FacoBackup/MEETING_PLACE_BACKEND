package br.meetingplace.server.routers.chat

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
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.messageRouter() {

    route("/api") {

        get(ChatPaths.MESSAGE) {
            val data = call.receive<RequestConversation>()
            call.respond(ChatReadService.readConversation(userID = data.userID, receiverID = data.receiverID, isGroup = data.isGroup, date = data.date,MessageDAO, AES))
        }

        post<RequestMessageCreation>(ChatPaths.MESSAGE) {
            call.respond(MessageFactoryService.createMessage(it, GroupMemberDAO, UserDAO, GroupDAO, MessageDAO, AES))
        }
        delete(ChatPaths.MESSAGE) {
            val data = call.receive<RequestMessage>()
            call.respond(MessageDeleteService.deleteMessage(data, MessageDAO))
        }
        put(ChatPaths.LIKE) {
            val data = call.receive<RequestMessage>()
            call.respond(MessageOpinionService.likeMessage(data, UserDAO, MessageDAO, MessageOpinionDAO))
        }
        put(ChatPaths.DISLIKE) {
            val data = call.receive<RequestMessage>()
            call.respond(MessageOpinionService.dislikeMessage(data, UserDAO, MessageDAO, MessageOpinionDAO))
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