package br.meetingplace.server.requests.chat

import br.meetingplace.server.requests.generic.Login

data class ChatComplexOperator(
        var message: String,
        var messageID: String,
        val source: ChatIdentifier,
        val receiver: ChatIdentifier,
        val login: Login
)