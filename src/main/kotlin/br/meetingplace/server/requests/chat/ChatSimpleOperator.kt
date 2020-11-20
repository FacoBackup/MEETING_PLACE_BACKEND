package br.meetingplace.server.requests.chat

import br.meetingplace.server.requests.generic.Login

data class ChatSimpleOperator(var messageID: String, val receiver: ChatIdentifier, val login: Login)