package br.meetingplace.server.modules.conversation.dto.response.notification

data class MessageNotificationDTO(
    val conversationID: Long,
    val messageID: Long,
    val subjectName: String?,
    val subjectImageURL: String?,
    val subjectID: Long?,
    val page: Long,
    val isGroup:Boolean,
    val creationDate: Long,
    val seenAt: Long?

)