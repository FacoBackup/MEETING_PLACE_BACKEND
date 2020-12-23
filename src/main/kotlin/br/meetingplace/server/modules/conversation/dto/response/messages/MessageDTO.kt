package br.meetingplace.server.modules.conversation.dto.response.messages

data class MessageDTO(val content: String,
                      val imageURL: String?,
                      val id: String,
                      val creatorID: String,
                      val conversationID: String,
                      val type: Short,
                      val valid: Long,
                      val creationDate: Long,
                      val seenByEveryone: Boolean
                        )