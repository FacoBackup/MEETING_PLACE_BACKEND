package br.meetingplace.server.routers.chat

import br.meetingplace.server.modules.messageTODO.dao.MessageDAO
import br.meetingplace.server.modules.messageTODO.services.delete.MessageDeleteService
import br.meetingplace.server.modules.messageTODO.services.opinion.MessageOpinionService
import br.meetingplace.server.modules.messageTODO.services.quote.MessageQuoteService
import br.meetingplace.server.modules.messageTODO.services.factory.MessageFactoryService
import br.meetingplace.server.modules.messageTODO.services.share.MessageShareService
import br.meetingplace.server.modules.messageTODO.entities.Message
import br.meetingplace.server.modules.messageTODO.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.messageTODO.dto.requests.RequestConversationMessage
import br.meetingplace.server.modules.messageTODO.dto.requests.RequestMessage
import br.meetingplace.server.modules.messageTODO.dto.requests.RequestConversation
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

fun Route.messageRouter() {

    route("/api") {

        get(MessagePaths.MESSAGE) {
            val data = call.receive<RequestConversation>()
            try {
                val chat = when(data.isGroup){
                    true-> transaction {
                        Message.select { (Message.creatorID eq data.userID) and (Message.groupReceiverID eq data.receiverID) }
                                .map { MessageDAO.mapMessage(it) }
                    }

                    false-> transaction {
                        Message.select {
                            ((Message.creatorID eq data.userID) and (Message.userReceiverID eq data.receiverID)) or
                            ((Message.creatorID eq data.receiverID) and (Message.userReceiverID eq data.userID))
                        }.map { MessageDAO.mapMessage(it) }
                    }
                }
                if (chat.isEmpty()) {
                    call.respond(Status(404, StatusMessages.NOT_FOUND))
                } else
                    call.respond(chat)
            }catch (normal: Exception){
                call.respond(Status(500, StatusMessages.INTERNAL_SERVER_ERROR))
            }catch (PSQL: PSQLException){
                call.respond(Status(500, StatusMessages.INTERNAL_SERVER_ERROR))
            }
        }

        post(MessagePaths.MESSAGE) {
            val data = call.receive<RequestMessageCreation>()
            call.respond(MessageFactoryService.createMessage(data))
        }
        delete(MessagePaths.MESSAGE) {
            val data = call.receive<RequestMessage>()
            call.respond(MessageDeleteService.deleteMessage(data))
        }
        put(MessagePaths.LIKE) {
            val data = call.receive<RequestMessage>()
            call.respond(MessageOpinionService.likeMessage(data))
        }
        put(MessagePaths.DISLIKE) {
            val data = call.receive<RequestMessage>()
            call.respond(MessageOpinionService.dislikeMessage(data))
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