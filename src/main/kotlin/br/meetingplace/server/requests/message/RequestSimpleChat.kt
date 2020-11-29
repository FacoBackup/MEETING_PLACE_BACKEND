package br.meetingplace.server.requests.message

data class RequestSimpleChat(val receiverID: String, val userID: String, val isGroup: Boolean)
