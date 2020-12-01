package br.meetingplace.server.request.dto.message

data class ConversationMessageDTO(
        val message: String,
        val imageURL: String?,
        val messageID: String,
        val sourceID: String,
        val receiverID: String,
        val userID: String
)