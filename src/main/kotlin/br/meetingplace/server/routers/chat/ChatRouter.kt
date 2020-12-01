package br.meetingplace.server.routers.chat

import br.meetingplace.server.modules.message.dao.MessageMapper
import br.meetingplace.server.modules.message.service.delete.DeleteMessageDAO
import br.meetingplace.server.modules.message.service.opinion.MessageOpinionDAO
import br.meetingplace.server.modules.message.service.quote.QuoteMessageDAO
import br.meetingplace.server.modules.message.service.factory.MessageFactoryDAO
import br.meetingplace.server.modules.message.service.share.ShareMessageDAO
import br.meetingplace.server.modules.message.entitie.Message
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
                                .map { MessageMapper.mapMessage(it) }
                    }

                    false-> transaction {
                        Message.select {
                            ((Message.creatorID eq data.userID) and (Message.userReceiverID eq data.receiverID)) or
                            ((Message.creatorID eq data.receiverID) and (Message.userReceiverID eq data.userID))
                        }.map { MessageMapper.mapMessage(it) }
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
            call.respond(MessageFactoryDAO.createMessage(data))
        }
        delete(MessagePaths.MESSAGE) {
            val data = call.receive<MessageDTO>()
            call.respond(DeleteMessageDAO.deleteMessage(data))
        }
        put(MessagePaths.LIKE) {
            val data = call.receive<MessageDTO>()
            call.respond(MessageOpinionDAO.likeMessage(data))
        }
        put(MessagePaths.DISLIKE) {
            val data = call.receive<MessageDTO>()
            call.respond(MessageOpinionDAO.dislikeMessage(data))
        }
        post(MessagePaths.QUOTE) {
            val data = call.receive<ConversationMessageDTO>()
            call.respond(QuoteMessageDAO.quoteMessage(data))
        }
        patch(MessagePaths.SHARE) {
            val data = call.receive<ConversationMessageDTO>()
            call.respond(ShareMessageDAO.shareMessage(data))
        }
    }
}