package br.meetingplace.server.routers.chat

import br.meetingplace.server.modules.messageTODO.dao.MessageDAO
import br.meetingplace.server.modules.messageTODO.service.delete.MessageDeleteService
import br.meetingplace.server.modules.messageTODO.service.opinion.MessageOpinionService
import br.meetingplace.server.modules.messageTODO.service.quote.MessageQuoteService
import br.meetingplace.server.modules.messageTODO.service.factory.MessageFactoryService
import br.meetingplace.server.modules.messageTODO.service.share.MessageShareService
import br.meetingplace.server.modules.messageTODO.entitie.Message
import br.meetingplace.server.request.dto.message.MessageCreationDTO
import br.meetingplace.server.request.dto.message.ConversationMessageDTO
import br.meetingplace.server.request.dto.message.MessageDTO
import br.meetingplace.server.request.dto.message.ConversationDTO
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
            val data = call.receive<ConversationDTO>()
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
            val data = call.receive<MessageCreationDTO>()
            call.respond(MessageFactoryService.createMessage(data))
        }
        delete(MessagePaths.MESSAGE) {
            val data = call.receive<MessageDTO>()
            call.respond(MessageDeleteService.deleteMessage(data))
        }
        put(MessagePaths.LIKE) {
            val data = call.receive<MessageDTO>()
            call.respond(MessageOpinionService.likeMessage(data))
        }
        put(MessagePaths.DISLIKE) {
            val data = call.receive<MessageDTO>()
            call.respond(MessageOpinionService.dislikeMessage(data))
        }
        post(MessagePaths.QUOTE) {
            val data = call.receive<ConversationMessageDTO>()
            call.respond(MessageQuoteService.quoteMessage(data))
        }
        patch(MessagePaths.SHARE) {
            val data = call.receive<ConversationMessageDTO>()
            call.respond(MessageShareService.shareMessage(data))
        }
    }
}