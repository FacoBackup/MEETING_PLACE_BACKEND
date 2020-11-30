package br.meetingplace.server.routers.chat

import br.meetingplace.server.db.mapper.message.MessageMapper
import br.meetingplace.server.modules.message.dao.delete.DeleteMessageDAO
import br.meetingplace.server.modules.message.dao.opinion.MessageOpinionDAO
import br.meetingplace.server.modules.message.dao.quote.QuoteMessageDAO
import br.meetingplace.server.modules.message.dao.factory.MessageFactoryDAO
import br.meetingplace.server.modules.message.dao.share.ShareMessageDAO
import br.meetingplace.server.modules.message.db.Message
import br.meetingplace.server.requests.message.RequestMessageCreation
import br.meetingplace.server.requests.message.RequestComplexChat
import br.meetingplace.server.requests.message.RequestMessageSimple
import br.meetingplace.server.requests.message.RequestSimpleChat
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
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
            val data = call.receive<RequestSimpleChat>()
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
            val data = call.receive<RequestMessageCreation>()
            call.respond(MessageFactoryDAO.createMessage(data))
        }
        delete(MessagePaths.MESSAGE) {
            val data = call.receive<RequestMessageSimple>()
            call.respond(DeleteMessageDAO.deleteMessage(data))
        }
        put(MessagePaths.LIKE) {
            val data = call.receive<RequestMessageSimple>()
            call.respond(MessageOpinionDAO.likeMessage(data))
        }
        put(MessagePaths.DISLIKE) {
            val data = call.receive<RequestMessageSimple>()
            call.respond(MessageOpinionDAO.dislikeMessage(data))
        }
        post(MessagePaths.QUOTE) {
            val data = call.receive<RequestComplexChat>()
            call.respond(QuoteMessageDAO.quoteMessage(data))
        }
        patch(MessagePaths.SHARE) {
            val data = call.receive<RequestComplexChat>()
            call.respond(ShareMessageDAO.shareMessage(data))
        }
    }
}