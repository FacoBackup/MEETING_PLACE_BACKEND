package br.meetingplace.server.modules.chat.dto.dependencies

import br.meetingplace.server.modules.chat.dto.dependencies.base.BaseChat
import br.meetingplace.server.modules.chat.dto.dependencies.base.BaseChatInterface
import br.meetingplace.server.modules.chat.dto.dependencies.data.MessageContent
import br.meetingplace.server.requests.chat.ChatComplexOperator
import br.meetingplace.server.requests.chat.ChatSimpleOperator

abstract class Controller : BaseChatInterface {

    private val data = BaseChat.getClass()

    override fun addMessage(message: MessageContent) {
        data.addMessage(message)
    }

    override fun deleteMessage(message: ChatSimpleOperator) {
        data.deleteMessage(message)
    }

    override fun favoriteMessage(message: ChatSimpleOperator) {
        data.favoriteMessage(message)
    }

    override fun quoteMessage(message: ChatComplexOperator, newId: String) {
        data.quoteMessage(message, newId)
    }

    override fun shareMessage(operations: ChatComplexOperator): String? {
        return data.shareMessage(operations)
    }

    override fun disfavorMessage(message: ChatSimpleOperator) {
        data.disfavorMessage(message)
    }

    override fun verifyMessage(idMessage: String): Boolean {
        return data.verifyMessage(idMessage)
    }
}