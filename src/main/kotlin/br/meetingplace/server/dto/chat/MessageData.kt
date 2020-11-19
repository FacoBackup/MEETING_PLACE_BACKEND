package br.meetingplace.server.dto.chat

import br.meetingplace.server.dto.Login
import br.meetingplace.server.services.chat.classes.dependencies.data.MessageType

data class MessageData(var message: String, val messageType: MessageType?, val receiver: ChatIdentifier, val login: Login)
