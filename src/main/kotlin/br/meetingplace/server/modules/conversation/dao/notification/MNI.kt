package br.meetingplace.server.modules.conversation.dao.notification

import br.meetingplace.server.modules.conversation.dto.response.notification.MessageNotificationDTO
import io.ktor.http.*

interface MNI {
    suspend fun create(requester: Long, messageID: Long, conversationID:Long , creationDate: Long, isGroup:Boolean): HttpStatusCode
    suspend fun read(requester: Long, page: Long): List<MessageNotificationDTO>
    suspend fun readLastPage(requester: Long): List<MessageNotificationDTO>
    suspend fun readUnseenQuantity(requester: Long): Long
}