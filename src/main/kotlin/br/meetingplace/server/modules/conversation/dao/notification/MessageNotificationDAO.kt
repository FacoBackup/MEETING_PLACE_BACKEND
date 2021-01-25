package br.meetingplace.server.modules.conversation.dao.notification
import br.meetingplace.server.modules.conversation.dto.response.notification.MessageNotificationDTO
import br.meetingplace.server.modules.conversation.entities.notification.MessageNotificationEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object MessageNotificationDAO: MNI {
    override suspend fun create(
        requester: Long,
        messageID: Long,
        conversationID: Long,
        creationDate: Long,
        isGroup: Boolean
    ): HttpStatusCode {
        return try {
            transaction {
                val lastPageQuantity: Int
                val currentPage: Long
                val size= MessageNotificationEntity.select{
                    (MessageNotificationEntity.userID eq requester)
                }.count()
                val lastPage = MessageNotificationEntity.select{
                    (MessageNotificationEntity.userID eq requester)
                }.limit(1, offset = if(size > 0) size-1 else size).map{ mapMessageNotification(it) }

                lastPageQuantity = if(lastPage.isNotEmpty()) MessageNotificationEntity.select{
                    (MessageNotificationEntity.userID eq requester) and (MessageNotificationEntity.page eq lastPage[0].page)
                }.count().toInt()
                else
                    -1
                currentPage = if(lastPageQuantity == -1) 1 else if(lastPageQuantity in 1..18) lastPage[0].page else lastPage[0].page+1

                MessageNotificationEntity.insert {
                    it[this.creationDate] = creationDate
                    it[this.conversationID] = conversationID
                    it[this.messageID] = messageID
                    it[seenAt] = null
                    it[this.isGroup] = isGroup
                    it[userID] = requester
                    it[page] = currentPage
                }
            }
            HttpStatusCode.Created
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override suspend fun readLastPage(requester: Long): List<MessageNotificationDTO> {
        return try {
            val lastPage = transaction {
                MessageNotificationEntity.select {
                    (MessageNotificationEntity.userID eq requester)
                }.orderBy(MessageNotificationEntity.page, SortOrder.DESC).map{ mapMessageNotification(it) }.firstOrNull()
            }

            return if(lastPage != null){
                val result = transaction {
                    MessageNotificationEntity.select {
                        (MessageNotificationEntity.userID eq requester) and
                                (MessageNotificationEntity.page eq lastPage.page)
                    }.map { mapMessageNotification(it) }
                }
                for(i in result.indices){
                    if(result[i].seenAt == null)
                        transaction {
                            MessageNotificationEntity.update({
                                (MessageNotificationEntity.messageID eq result[i].messageID) and
                                        (MessageNotificationEntity.conversationID eq result[i].conversationID)
                            }) {
                                it[seenAt] = System.currentTimeMillis()
                            }
                        }
                }
                result
            }
            else
                listOf()
        }catch (e: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }

    override suspend fun readUnseenQuantity(requester: Long): Long {
        return try {
            transaction {
                MessageNotificationEntity.select {
                    (MessageNotificationEntity.userID eq requester) and
                            (MessageNotificationEntity.seenAt eq null)
                }.count()
            }

        }catch (e: Exception){
            0
        }catch (psql: PSQLException){
            0
        }
    }

    override suspend fun read(requester: Long, page: Long): List<MessageNotificationDTO> {
        return try {
            val result = transaction {
                MessageNotificationEntity.select {
                    (MessageNotificationEntity.userID eq requester) and
                            (MessageNotificationEntity.page eq page)
                }.orderBy(MessageNotificationEntity.creationDate, SortOrder.DESC).map { mapMessageNotification(it) }
            }
            for(i in result.indices){
                if(result[i].seenAt == null)
                    transaction {
                        MessageNotificationEntity.update({
                            (MessageNotificationEntity.messageID eq result[i].messageID) and
                                    (MessageNotificationEntity.conversationID eq result[i].conversationID)
                        }) {
                            it[seenAt] = System.currentTimeMillis()
                        }
                    }
            }
            result
        }catch (e: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }

    }
    private fun mapMessageNotification(it: ResultRow): MessageNotificationDTO{
        return MessageNotificationDTO(
            subjectName = null,
            subjectImageURL =null,
            seenAt = it[MessageNotificationEntity.seenAt],
            conversationID =it[MessageNotificationEntity.conversationID],
            messageID = it[MessageNotificationEntity.messageID],
            creationDate = it[MessageNotificationEntity.creationDate],
            page = it[MessageNotificationEntity.page],
            isGroup = it[MessageNotificationEntity.isGroup],
            subjectID = null
        )
    }
}