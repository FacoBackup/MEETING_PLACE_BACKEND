package br.meetingplace.server.routers.chat

import br.meetingplace.server.modules.group.dao.GroupDAO
import br.meetingplace.server.modules.group.dao.member.GroupMemberDAO
import br.meetingplace.server.modules.message.dao.MessageDAO
import br.meetingplace.server.modules.message.dao.opinions.MessageOpinionDAO
import br.meetingplace.server.modules.message.services.delete.MessageDeleteService
import br.meetingplace.server.modules.message.services.opinion.MessageOpinionService
import br.meetingplace.server.modules.message.services.quote.MessageQuoteService
import br.meetingplace.server.modules.message.services.factory.MessageFactoryService
import br.meetingplace.server.modules.message.services.share.MessageShareService
import br.meetingplace.server.modules.message.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.message.dto.requests.RequestConversationMessage
import br.meetingplace.server.modules.message.dto.requests.RequestMessage
import br.meetingplace.server.modules.message.dto.requests.RequestConversation
import br.meetingplace.server.modules.user.dao.UserDAO
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.messageRouter() {

    route("/api") {

        get(MessagePaths.MESSAGE) {
            val data = call.receive<RequestConversation>()
            call.respond(MessageDAO.readAllConversation(userID = data.userID, receiverID = data.receiverID, isGroup = data.isGroup))
        }

        post(MessagePaths.MESSAGE) {
            val data = call.receive<RequestMessageCreation>()
            call.respond(MessageFactoryService.createMessage(data, GroupMemberDAO, UserDAO, GroupDAO, MessageDAO))
        }
        delete(MessagePaths.MESSAGE) {
            val data = call.receive<RequestMessage>()
            call.respond(MessageDeleteService.deleteMessage(data, MessageDAO))
        }
        put(MessagePaths.LIKE) {
            val data = call.receive<RequestMessage>()
            call.respond(MessageOpinionService.likeMessage(data, UserDAO, MessageDAO, MessageOpinionDAO))
        }
        put(MessagePaths.DISLIKE) {
            val data = call.receive<RequestMessage>()
            call.respond(MessageOpinionService.dislikeMessage(data, UserDAO, MessageDAO, MessageOpinionDAO))
        }
        post(MessagePaths.QUOTE) {
            val data = call.receive<RequestConversationMessage>()
            call.respond(MessageQuoteService.quoteMessage(data))
        }
        patch(MessagePaths.SHARE) {
            val data = call.receive<RequestConversationMessage>()
            call.respond(MessageShareService.shareMessage(data))
        }
    }
}