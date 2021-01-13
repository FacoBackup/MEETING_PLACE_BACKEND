package br.meetingplace.server.modules.conversation.routes

import br.meetingplace.server.methods.AES
import br.meetingplace.server.modules.conversation.dao.conversation.member.ConversationMemberDAO
import br.meetingplace.server.modules.conversation.dao.conversation.owners.ConversationOwnersDAO
import br.meetingplace.server.modules.conversation.dao.messages.MessageDAO
import br.meetingplace.server.modules.conversation.dao.messages.opinions.MessageOpinionDAO
import br.meetingplace.server.modules.conversation.dao.messages.status.MessageStatusDAO
import br.meetingplace.server.modules.conversation.dto.requests.messages.RequestShareMessage
import br.meetingplace.server.modules.conversation.dto.requests.messages.RequestMessage
import br.meetingplace.server.modules.conversation.dto.requests.messages.RequestMessagesDTO
import br.meetingplace.server.modules.conversation.services.message.delete.MessageDeleteService
import br.meetingplace.server.modules.conversation.services.message.opinion.MessageOpinionService
import br.meetingplace.server.modules.conversation.services.message.quote.MessageQuoteService
import br.meetingplace.server.modules.conversation.services.message.read.MessageReadService
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

        //FETCH
        get("/fetch/unseen/messages"){
            val data = call.receive<RequestMessagesDTO>()
            val log = call.log
            if(log != null)
                when(data.isUser){
                    true->{
                        call.respond(MessageReadService.readNewUserMessages(requester = log.userID, userID= data.subjectID, conversationOwnerDAO = ConversationOwnersDAO, decryption = AES, messageStatusDAO = MessageStatusDAO, messageDAO = MessageDAO, userDAO = UserDAO))

                    }
                    false->{
                        call.respond(HttpStatusCode.NotImplemented)
                    }
                }
            else call.respond(HttpStatusCode.Unauthorized)
        }

        get("/fetch/by/page"){
            val data = call.receive<RequestMessagesDTO>()
            val log = call.log
            if(log != null)
                when(data.isUser){
                    true->{

                        call.respond(MessageReadService.readUserMessages(requester = log.userID, data= data, conversationOwnerDAO = ConversationOwnersDAO, decryption = AES, messageStatusDAO = MessageStatusDAO, messageDAO = MessageDAO))
                    }
                    false->{
                        call.respond(HttpStatusCode.NotImplemented)
                    }
                }
            else call.respond(HttpStatusCode.Unauthorized)
        }

        //FETCH


        delete("/message") {
            val data = call.receive<RequestMessage>()
            val log = call.log
            if(log != null)
                call.respond(MessageDeleteService.deleteMessage(requester = log.userID,data, MessageDAO))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch("/seen/by/everyone/check"){
            val data = call.receive<RequestMessage>()
            val log = call.log
            if(log != null)
                call.respond(MessageStatusDAO.seenByEveryoneByMessage(messageID = data.messageID, conversationID = data.conversationID))
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
        post<RequestShareMessage>("/message/quote") {

            val log = call.log
            if(log != null)
                call.respond(MessageQuoteService.quoteMessage(it))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        post<RequestShareMessage>("/message/share") {
            val log = call.log
            if(log != null)
                call.respond(MessageShareService.shareMessage(it))
            else call.respond(HttpStatusCode.Unauthorized)

        }
    }
}