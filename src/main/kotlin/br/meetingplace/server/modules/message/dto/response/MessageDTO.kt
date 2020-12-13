package br.meetingplace.server.modules.message.dto.response

data class MessageDTO(var content: String,
                      val imageURL: String?,
                      val id: String,
                      val creatorID: String,
                      val type: Short,
                      val receiverID: String?,
                      val valid: Int,
                      val groupID: String?)