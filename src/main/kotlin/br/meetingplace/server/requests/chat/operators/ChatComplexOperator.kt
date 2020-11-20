package br.meetingplace.server.requests.chat.operators

import br.meetingplace.server.requests.chat.data.ChatContent
import br.meetingplace.server.requests.chat.data.ChatIdentifier
import br.meetingplace.server.requests.generic.data.Login

data class ChatComplexOperator(
        val content: ChatContent,
        val messageID: String,
        val source: ChatIdentifier,
        val receiver: ChatIdentifier,
        val login: Login
)