package br.meetingplace.server.modules.chat.classes.message

import org.jetbrains.exposed.sql.Table

data class MessageOpinions(var liked: Boolean, val chatID: String, val messageID: String, val userID: String): Table()
