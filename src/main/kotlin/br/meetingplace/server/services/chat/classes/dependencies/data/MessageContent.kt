package br.meetingplace.server.services.chat.classes.dependencies.data

data class MessageContent(var content: String, val ID: String, val creator: String, val type: MessageType)