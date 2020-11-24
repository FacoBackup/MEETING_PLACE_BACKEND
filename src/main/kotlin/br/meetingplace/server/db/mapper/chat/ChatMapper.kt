package br.meetingplace.server.db.mapper.chat

import br.meetingplace.server.modules.chat.db.Chat
import br.meetingplace.server.modules.chat.db.ChatOwner
import br.meetingplace.server.modules.chat.db.Message
import br.meetingplace.server.modules.chat.db.MessageOpinions
import br.meetingplace.server.modules.chat.dto.ChatDTO
import br.meetingplace.server.modules.chat.dto.ChatOwnerDTO
import br.meetingplace.server.modules.chat.dto.MessageDTO
import br.meetingplace.server.modules.chat.dto.MessageOpinionsDTO
import org.jetbrains.exposed.sql.ResultRow

class ChatMapper: ChatMapperInterface{
    override fun mapChat(it: ResultRow): ChatDTO {
        return ChatDTO(id = it[Chat.id], groupID = it[Chat.groupID], creationDate = it[Chat.creationDate].toString("dd-MM-yyyy"))
    }
    override fun mapChatOwners(it: ResultRow): ChatOwnerDTO {
        return ChatOwnerDTO(chatID = it[ChatOwner.chatID],userID =  it[ChatOwner.userID], receiverID = it[ChatOwner.receiverID])
    }
    override fun mapMessage(it: ResultRow): MessageOpinionsDTO {
        return MessageOpinionsDTO(liked = it[MessageOpinions.liked], chatID = it[MessageOpinions.chatID],messageID = it[MessageOpinions.messageID], userID = it[MessageOpinions.userID])
    }
    override fun mapMessageOpinions(it: ResultRow): MessageDTO {
        return MessageDTO(content = it[Message.content], imageURL = it[Message.imageURL], id = it[Message.id], creationDate = it[Message.creationDate].toString("dd-MM-yyyy"), creatorID = it[Message.creatorID], chatID = it[Message.chatID], type =  it[Message.type])
    }
}