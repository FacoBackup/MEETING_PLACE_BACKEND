package br.meetingplace.server.modules.chatTODOTRANSACTIONS.dto

data class MessageDTO(var content: String, val imageURL: String?, val id: String,
                      val creatorID: String, val type: Short, val chatID: String,
                      val creationDate: String)