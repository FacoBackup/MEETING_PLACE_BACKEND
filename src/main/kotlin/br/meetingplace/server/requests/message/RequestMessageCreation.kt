package br.meetingplace.server.requests.message

data class RequestMessageCreation(val message: String, val imageURL: String?, val receiverID: String, val userID: String, val isGroup: Boolean)
