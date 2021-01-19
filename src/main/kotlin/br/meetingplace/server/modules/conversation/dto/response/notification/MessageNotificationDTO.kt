package br.meetingplace.server.modules.conversation.dto.response.notification

data class MessageNotificationDTO(
    val conversationID: String,
    val messageID: String,
    val subjectName: String?,
    val subjectImageURL: String?,
    val subjectID: String?,
    val page: Long,
    val isGroup:Boolean,
    val creationDate: Long,
    val seenAt: Long?

)