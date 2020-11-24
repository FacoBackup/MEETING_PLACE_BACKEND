package br.meetingplace.server.requests.chat.operators

data class ChatSimpleOperator(var messageID: String, val receiverID: String, val userID: String)