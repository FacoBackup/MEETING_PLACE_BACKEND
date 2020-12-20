package br.meetingplace.server.modules.conversation.routes

import br.meetingplace.server.modules.conversation.dao.conversation.member.ConversationMemberDAO
import br.meetingplace.server.modules.conversation.dao.messages.MessageDAO
import br.meetingplace.server.modules.conversation.dao.messages.opinions.MessageOpinionDAO
import br.meetingplace.server.modules.conversation.dto.requests.RequestConversationMessage
import br.meetingplace.server.modules.conversation.dto.requests.RequestMessage
import br.meetingplace.server.modules.conversation.services.message.delete.MessageDeleteService
import br.meetingplace.server.modules.conversation.services.message.opinion.MessageOpinionService
import br.meetingplace.server.modules.conversation.services.message.quote.MessageQuoteService
import br.meetingplace.server.modules.conversation.services.message.share.MessageShareService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.messageRouter(){
    route("/api"){
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