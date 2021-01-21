package br.meetingplace.server.modules.conversation.dto.response.conversation

data class ConversationOwnersDTO(val primaryUserID: Long, val secondaryUserID: Long, val conversationID: Long)
