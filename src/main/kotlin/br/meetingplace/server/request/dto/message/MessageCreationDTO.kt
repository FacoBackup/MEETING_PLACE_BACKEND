package br.meetingplace.server.request.dto.message

data class MessageCreationDTO(val message: String, val imageURL: String?, val receiverID: String, val userID: String, val isGroup: Boolean)
