package br.meetingplace.server.request.dto.message

data class ConversationDTO(val receiverID: String, val userID: String, val isGroup: Boolean)
