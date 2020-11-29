package br.meetingplace.server.modules.message.dto

data class MessageOpinionsDTO(var liked: Boolean, val messageID: String, val userID: String)
