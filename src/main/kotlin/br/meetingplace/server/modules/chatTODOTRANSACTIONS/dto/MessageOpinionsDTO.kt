package br.meetingplace.server.modules.chatTODOTRANSACTIONS.dto

data class MessageOpinionsDTO(var liked: Boolean, val chatID: String, val messageID: String, val userID: String)
