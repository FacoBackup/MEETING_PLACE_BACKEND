package br.meetingplace.server.modules.conversation.dto.response.messages

data class MessageDTO(val content: String,
                      val imageURL: String?,
                      val id: Long,
                      val creatorID: Long,
                      val receiverAsUserID: Long?,
                      val conversationID: Long,
                      val isShared: Boolean,
                      val isQuoted:Boolean,
                      val creationDate: Long,
                      val seenByEveryone: Boolean,
                      val page: Long
                      )