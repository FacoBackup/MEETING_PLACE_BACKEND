package br.meetingplace.server.modules.conversation.dao.notification

import br.meetingplace.server.modules.conversation.dao.messages.MessageDAO
import br.meetingplace.server.modules.conversation.dto.response.notification.MessageNotificationDTO
import br.meetingplace.server.modules.conversation.entities.messages.MessageEntity
import br.meetingplace.server.modules.conversation.entities.notification.MessageNotificationEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object MessageNotificationDAO: MNI {
    override suspend fun create(
        requester: String,
        subjectID: String,
        isGroup: Boolean,

        creationDate: Long
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

                    when(isGroup){
                        true -> it[subjectAsGroupID] = subjectID
                        false -> it[subjectAsUserID] = subjectID
                    }
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

    override suspend fun readLastPage(requester: String): List<MessageNotificationDTO> {
        return try {
            val lastPage = transaction {
                MessageNotificationEntity.select {
                    (MessageNotificationEntity.userID eq requester)
                }.orderBy(MessageEntity.page, SortOrder.DESC).map{ mapMessageNotification(it) }.firstOrNull()
            }



            return if(lastPage != null){
                transaction {
                    MessageNotificationEntity.select {
                        (MessageNotificationEntity.userID eq requester) and
                                (MessageNotificationEntity.page eq lastPage.page)
                    }.map { mapMessageNotification(it) }
                }
            }
            else
                listOf()
        }catch (e: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun read(requester: String, page: Long): List<MessageNotificationDTO> {
        return try {
            transaction {
                MessageNotificationEntity.select {
                    (MessageNotificationEntity.userID eq requester) and
                            (MessageNotificationEntity.page eq page)
                }.orderBy(MessageNotificationEntity.creationDate, SortOrder.DESC).map { mapMessageNotification(it) }
            }
        }catch (e: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }

    }
    private fun mapMessageNotification(it: ResultRow): MessageNotificationDTO{
        return MessageNotificationDTO(
            subjectID = if(!it[MessageNotificationEntity.subjectAsGroupID].isNullOrBlank()) it[MessageNotificationEntity.subjectAsGroupID]!! else it[MessageNotificationEntity.subjectAsUserID]!!,
            isGroup = !it[MessageNotificationEntity.subjectAsGroupID].isNullOrBlank(),
            subjectName = null,
            subjectImageURL = null,
            creationDate = it[MessageNotificationEntity.creationDate],
            page = it[MessageNotificationEntity.page]
        )
    }
}