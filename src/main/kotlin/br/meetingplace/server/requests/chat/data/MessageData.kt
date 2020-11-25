package br.meetingplace.server.requests.chat.data

data class MessageData(val message: String, val imageURL: String?, val chatID: String, val userID: String)
