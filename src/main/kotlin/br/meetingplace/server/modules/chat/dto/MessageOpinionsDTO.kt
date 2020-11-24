package br.meetingplace.server.modules.chat.dto

import org.jetbrains.exposed.sql.Table

data class MessageOpinionsDTO(var liked: Boolean, val chatID: String, val messageID: String, val userID: String)
