package br.meetingplace.server.modules.conversation.dao.notification

import br.meetingplace.server.modules.conversation.dto.response.notification.MessageNotificationDTO
import io.ktor.http.*

interface MNI {
    suspend fun create(requester: String, messageID: String, conversationID:String , creationDate: Long, isGroup:Boolean): HttpStatusCode
    suspend fun read(requester: String, page: Long): List<MessageNotificationDTO>
    suspend fun readLastPage(requester: String): List<MessageNotificationDTO>
    suspend fun readUnseenQuantity(requester: String): Long
}