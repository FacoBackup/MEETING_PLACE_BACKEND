package br.meetingplace.server.modules.conversation.dto.response.messages

data class MessageStatusDTO(val conversationID: String, val userID: String, val messageID: String, val seen: Boolean)
