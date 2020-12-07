package br.meetingplace.server.modules.message.dto.requests

data class RequestConversation(val receiverID: String, val userID: String, val isGroup: Boolean, val date: String)
// date = "yyyy-MM-dd"
