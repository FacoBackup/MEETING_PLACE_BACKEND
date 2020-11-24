package br.meetingplace.server.requests.chat.operators

data class ChatComplexOperator(
        val message: String,
        val imageURL: String?,
        val messageID: String,
        val sourceID: String,
        val receiverID: String,
        val userID: String
)