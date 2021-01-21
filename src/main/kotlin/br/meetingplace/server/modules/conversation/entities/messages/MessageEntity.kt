package br.meetingplace.server.modules.conversation.entities.messages

import br.meetingplace.server.modules.conversation.entities.conversation.ConversationEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessageEntity: Table("messages"){
    val content = text("message_content")
    val image = text("image").nullable()
    val id = long("message_pk").autoIncrement()
    val creatorID = long("creator_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val conversationID = long("conversation_pk").references(ConversationEntity.id, onDelete = ReferenceOption.CASCADE)
    val isShared = bool("is_shared")
    val isQuoted = bool("is_quoted")
    val creationDate = long("date_of_creation")
    val page = long("page")
    val seenByEveryone = bool("seen_by_everyone")
    override val primaryKey = PrimaryKey(id)
}