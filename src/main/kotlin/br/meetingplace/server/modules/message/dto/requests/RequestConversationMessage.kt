package br.meetingplace.server.modules.message.dto.requests

data class RequestConversationMessage(
        val message: String,
        val imageURL: String?,
        val messageID: String,
        val sourceID: String,
        val receiverID: String,
        val userID: String
)