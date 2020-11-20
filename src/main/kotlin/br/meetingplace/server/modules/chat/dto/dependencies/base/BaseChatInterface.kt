package br.meetingplace.server.modules.chat.dto.dependencies.base

import br.meetingplace.server.modules.chat.dto.dependencies.data.MessageContent
import br.meetingplace.server.routers.chat.requests.ChatComplexOperator
import br.meetingplace.server.routers.chat.requests.ChatSimpleOperator

interface BaseChatInterface {
    fun addMessage(message: MessageContent)
    fun deleteMessage(message: ChatSimpleOperator)
    fun shareMessage(operations: ChatComplexOperator): String?
    fun quoteMessage(message: ChatComplexOperator, newId: String)
    fun favoriteMessage(message: ChatSimpleOperator)
    fun disfavorMessage(message: ChatSimpleOperator)
    fun verifyMessage(idMessage: String): Boolean
}