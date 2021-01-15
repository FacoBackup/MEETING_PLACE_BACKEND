package br.meetingplace.server.modules.conversation.dto.response.notification

data class MessageNotificationDTO(
    val subjectID: String,
    val subjectName: String?,
    val subjectImageURL: String?,
    val page: Long,
    val isGroup:Boolean,
    val creationDate: Long
)