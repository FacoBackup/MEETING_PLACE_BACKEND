package br.meetingplace.server.modules.conversation.dto.requests.messages

data class RequestMessagesDTO(
        val subjectID: Long,
        val isUser: Boolean,
        val page: Long?
)