package br.meetingplace.server.modules.conversation.entities.notification

import br.meetingplace.server.modules.conversation.entities.conversation.ConversationEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessageNotificationEntity: Table("message_notification") {
    val subjectAsUserID = varchar("subject_user_id", 320).references(UserEntity.email, onDelete = ReferenceOption.CASCADE).nullable()
    val subjectAsGroupID = varchar("subject_group_id", 36).references(ConversationEntity.id, onDelete = ReferenceOption.CASCADE).nullable()
    val userID = varchar("user_id", 320).references(UserEntity.email, onDelete = ReferenceOption.CASCADE)


    val creationDate = long("creation_date")

    val page = long("page")
}