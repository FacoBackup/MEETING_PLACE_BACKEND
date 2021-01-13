package br.meetingplace.server.modules.conversation.dto.requests.messages

data class RequestMessagesDTO(
        val subjectID: String,
        val isUser: Boolean,
        val page: Int
)