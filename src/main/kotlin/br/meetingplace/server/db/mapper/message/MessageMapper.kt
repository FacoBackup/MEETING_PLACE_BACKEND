package br.meetingplace.server.db.mapper.message

import br.meetingplace.server.modules.message.db.Message
import br.meetingplace.server.modules.message.db.MessageOpinions
import br.meetingplace.server.modules.message.dto.MessageDTO
import br.meetingplace.server.modules.message.dto.MessageOpinionsDTO
import org.jetbrains.exposed.sql.ResultRow

object MessageMapper: MessageMapperInterface{
    override fun mapMessage(it: ResultRow): MessageOpinionsDTO {
        return MessageOpinionsDTO(liked = it[MessageOpinions.liked],messageID = it[MessageOpinions.messageID], userID = it[MessageOpinions.userID])
    }
    override fun mapMessageOpinions(it: ResultRow): MessageDTO {
        return MessageDTO(content = it[Message.content], imageURL = it[Message.imageURL],
                id = it[Message.id], creationDate = it[Message.creationDate].toString("dd-MM-yyyy"),
                creatorID = it[Message.creatorID], type =  it[Message.type],receiverID = it[Message.userReceiverID], groupID = it[Message.groupReceiverID])
    }
}