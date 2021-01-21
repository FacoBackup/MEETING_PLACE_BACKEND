package br.meetingplace.server.modules.conversation.dto.response.messages

data class MessageStatusDTO(
    val conversationID: Long,
    val userID: Long,
    val messageID: Long,
    val seen: Boolean,
    val seenAt: Long?
    )
