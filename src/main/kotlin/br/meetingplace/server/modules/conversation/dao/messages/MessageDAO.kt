package br.meetingplace.server.modules.conversation.dao.messages

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageDTO
import br.meetingplace.server.modules.conversation.entities.messages.MessageEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object MessageDAO: MI{
    override suspend fun readLastMessage(conversationID: Long, userID: Long): String? {
        return try {
            val result = transaction {
                MessageEntity.select {
                    (MessageEntity.conversationID eq conversationID) and
                    (MessageEntity.creationDate.lessEq(System.currentTimeMillis())) and
                    (MessageEntity.creatorID neq userID)
                }.map { mapMessage(it) }.firstOrNull()
            }
            result?.content
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    override suspend fun readLastPage(conversationID: Long): List<MessageDTO> {
        return try {

            val lastPage = transaction {
                MessageEntity.select {
                    (MessageEntity.conversationID eq conversationID)
                }.orderBy(MessageEntity.page, SortOrder.DESC).map{ mapMessage(it) }.firstOrNull()
            }
            return if(lastPage != null){
                transaction {
                    MessageEntity.select {
                        (MessageEntity.conversationID eq conversationID) and
                                (MessageEntity.page eq lastPage.page)
                    }.map { mapMessage(it) }
                }
            }
            else
                listOf()
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun readByPage(conversationID: Long, page: Long): List<MessageDTO> {
        return try {
            transaction {
                MessageEntity.select {
                    (MessageEntity.conversationID eq conversationID) and
                            (MessageEntity.page eq page)
                }.map { mapMessage(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }

    override suspend fun create(message: String, imageURL: String?, conversationID: Long, creator: Long): Long? {
        return try {
            transaction {
                val lastPageQuantity: Int
                val currentPage: Long
                val size= MessageEntity.select{
                    (MessageEntity.conversationID eq conversationID)
                }.count()
                val lastPage = MessageEntity.select{
                    (MessageEntity.conversationID eq conversationID)
                }.limit(1, offset = if(size > 0) size-1 else size).map{ mapMessage(it) }

                lastPageQuantity = if(lastPage.isNotEmpty()) MessageEntity.select{
                    (MessageEntity.conversationID eq conversationID) and
                            (MessageEntity.page eq lastPage[0].page)
                }.count().toInt()
                else
                    -1
                currentPage = if(lastPageQuantity == -1) 1 else if(lastPageQuantity in 1..18) lastPage[0].page else lastPage[0].page+1

                MessageEntity.insert {
                    it[content] = message
                    it[this.creatorID] = creator
                    it[this.image] = imageURL
                    it[isShared] = false
                    it[isQuoted] = false
                    it[this.conversationID] = conversationID
                    it[creationDate] = System.currentTimeMillis()
                    it[this.seenByEveryone] = false
                    it[page] = currentPage
                } get MessageEntity.id
            }

        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override suspend fun update(messageID: Long): HttpStatusCode {
        return try {
            transaction {
                MessageEntity.update( {
                    MessageEntity.id eq messageID
                }){
                    it[this.seenByEveryone] = true
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override suspend fun delete(messageID: Long): HttpStatusCode {
        return try {
            transaction {
                MessageEntity.deleteWhere {
                    MessageEntity.id eq messageID
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun check(messageID: Long): Boolean {
        return try {
            !transaction {
                MessageEntity.select {
                    MessageEntity.id eq messageID
                }.empty()
            }
        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }
    override suspend fun read(messageID: Long): MessageDTO? {
        return try {

            transaction {
                MessageEntity.select {
                    MessageEntity.id eq messageID
                }.map { mapMessage(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    override suspend fun readAllConversation(userID: Long, conversationID: Long): List<MessageDTO> {
        return try {
            val conversation  = mutableListOf<MessageDTO>()
            conversation.addAll(transaction {
                MessageEntity.select {
                    MessageEntity.conversationID eq conversationID
                }.map { mapMessage(it) }
            })
            return conversation
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    private fun mapMessage(it: ResultRow): MessageDTO {
        return MessageDTO(
            content = it[MessageEntity.content],
            imageURL = it[MessageEntity.image],
            id = it[MessageEntity.id],

            creatorID = it[MessageEntity.creatorID],
            isShared =  it[MessageEntity.isShared],
            isQuoted = it[MessageEntity.isQuoted],
            conversationID = it[MessageEntity.conversationID],
            creationDate = it[MessageEntity.creationDate],
            seenByEveryone = it[MessageEntity.seenByEveryone],
            receiverAsUserID = null,
            page = it[MessageEntity.page]
        )
    }
}