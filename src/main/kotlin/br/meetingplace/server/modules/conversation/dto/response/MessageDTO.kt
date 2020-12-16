package br.meetingplace.server.modules.conversation.dto.response

data class MessageDTO(val content: String,
                      val imageURL: String?,
                      val id: String,
                      val creatorID: String,
                      val conversationID: String?,
                      val type: Short,
                      val valid: Int,
                      val read: Boolean,
                      val received: Boolean,
                      val creationDate: String)