package br.meetingplace.server.requests.chat.operators

import br.meetingplace.server.requests.chat.data.ChatIdentifier
import br.meetingplace.server.requests.generic.data.Login

data class ChatSimpleOperator(var messageID: String, val receiver: ChatIdentifier, val login: Login)