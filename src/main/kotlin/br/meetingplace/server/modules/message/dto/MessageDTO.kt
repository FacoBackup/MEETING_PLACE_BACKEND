package br.meetingplace.server.modules.message.dto

data class MessageDTO(var content: String, val imageURL: String?, val id: String,
                      val creatorID: String, val type: Short, val receiverID: String?,
                      val creationDate: String, val groupID: String?)