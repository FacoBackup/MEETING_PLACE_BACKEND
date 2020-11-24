package br.meetingplace.server.modules.chat.dto

import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime

data class MessageDTO(var content: String, val imageURL: String?, val id: String,
                      val creatorID: String, val type: Short, val chatID: String,
                      val creationDate: String)