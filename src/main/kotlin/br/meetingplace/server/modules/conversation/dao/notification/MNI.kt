package br.meetingplace.server.modules.conversation.dao.notification

import br.meetingplace.server.modules.conversation.dto.response.notification.MessageNotificationDTO
import io.ktor.http.*

interface MNI {
    suspend fun create(requester: String, subjectID: String,isGroup: Boolean, creationDate: Long): HttpStatusCode
    suspend fun read(requester: String, page: Long): List<MessageNotificationDTO>
    suspend fun readLastPage(requester: String): List<MessageNotificationDTO>
}