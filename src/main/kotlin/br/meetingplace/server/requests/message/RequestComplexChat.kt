package br.meetingplace.server.requests.message

data class RequestComplexChat(
        val message: String,
        val imageURL: String?,
        val messageID: String,
        val sourceID: String,
        val receiverID: String,
        val userID: String
)