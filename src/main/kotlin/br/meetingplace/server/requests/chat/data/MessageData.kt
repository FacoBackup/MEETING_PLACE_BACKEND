package br.meetingplace.server.requests.chat.data

data class MessageData(val message: String, val imageURL: String?, val receiverID: String, val userID: String)
