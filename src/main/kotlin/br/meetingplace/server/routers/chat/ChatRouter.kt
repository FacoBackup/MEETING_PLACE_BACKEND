package br.meetingplace.server.routers.chat

import br.meetingplace.server.db.mapper.chat.ChatMapper
import br.meetingplace.server.modules.chat.dao.delete.DeleteMessage
import br.meetingplace.server.modules.chat.dao.dislike.DislikeMessage
import br.meetingplace.server.modules.chat.dao.like.LikeMessage
import br.meetingplace.server.modules.chat.dao.quote.QuoteMessage
import br.meetingplace.server.modules.chat.dao.send.SendMessage
import br.meetingplace.server.modules.chat.dao.share.ShareMessage
import br.meetingplace.server.modules.chat.db.Chat
import br.meetingplace.server.requests.chat.data.MessageData
import br.meetingplace.server.requests.chat.operators.ChatComplexOperator
import br.meetingplace.server.requests.chat.operators.ChatSimpleOperator
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.routers.chat.paths.ChatPaths
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.chatRouter() {

    route("/api") {

        get(ChatPaths.CHAT) {
            val data = call.receive<ChatSimpleOperator>()
            val chat = transaction { Chat.select { Chat.id eq data.chatID }.map { ChatMapper.mapChat(it) } }.firstOrNull()
            if (chat == null) {
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            } else
                call.respond(chat)
        }
        post(ChatPaths.CHAT) {
            val data = call.receive<MessageData>()
            call.respond(SendMessage.sendMessage(data, chatMapper = ChatMapper))
        }
        delete(ChatPaths.CHAT) {
            val data = call.receive<ChatSimpleOperator>()
            call.respond(DeleteMessage.deleteMessage(data))
        }

        post(ChatPaths.QUOTE) {
            val data = call.receive<ChatComplexOperator>()
            call.respond(QuoteMessage.quoteMessage(data))
        }

        patch(ChatPaths.LIKE) {
            val data = call.receive<ChatSimpleOperator>()
            call.respond(LikeMessage.favoriteMessage(data))
        }
        patch(ChatPaths.DISLIKE) {
            val data = call.receive<ChatSimpleOperator>()
            call.respond(DislikeMessage.dislikeMessage(data))
        }
        patch(ChatPaths.SHARE) {
            val data = call.receive<ChatComplexOperator>()
            call.respond(ShareMessage.shareMessage(data))
        }
    }
}