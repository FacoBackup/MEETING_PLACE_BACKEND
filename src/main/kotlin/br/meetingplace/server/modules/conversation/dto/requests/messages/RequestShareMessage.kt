package br.meetingplace.server.modules.conversation.dto.requests.messages

data class RequestShareMessage(
        val message: String,
        val imageURL: String?,
        val messageID: String,
        val sourceID: String,
        val receiverID: String,
)