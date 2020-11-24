package br.meetingplace.server.modules.chat.classes.message

import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime

data class Message(var content: String, val imageURL: String?, val ID: String, val creator: String, val type: MessageType, val chatID: String, private val creationDate: LocalDateTime): Table()