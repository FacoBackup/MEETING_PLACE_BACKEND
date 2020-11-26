package br.meetingplace.server.modules.chat.dto

data class MessageOpinionsDTO(var liked: Boolean, val chatID: String, val messageID: String, val userID: String)
