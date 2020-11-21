package br.meetingplace.server.modules.chat.dto.dependencies.base

class BaseChat private constructor(){
    companion object {
        private val Class = BaseChat()
        fun getClass() = Class
    }


//
//
//    override fun verifyMessage(idMessage: String): Boolean {
//        return idMessage in idMessages
//    }
//
//    override fun addMessage(message: Content) {
//        if (message.ID !in idMessages) {
//            messages.add(message)
//            idMessages.add(message.ID)
//        }
//    }
//
//    override fun deleteMessage(message: ChatSimpleOperator) {
//        if (message.messageID in idMessages) {
//            if (message.messageID in favoriteMessages)
//                disfavorMessage(message)
//            messages.removeAt(idMessages.indexOf(message.messageID))
//            idMessages.remove(message.messageID)
//        }
//    }
//
//    override fun shareMessage(operations: ChatComplexOperator): String? {
//        val index = idMessages.indexOf(operations.messageID)
//        return if (index != -1)
//            messages[index].content
//        else null
//    }
//
//    override fun quoteMessage(message: ChatComplexOperator, newId: String) {
//        val index = idMessages.indexOf(message.messageID)
//        if (index != -1) {
//            message.content.message = "|${messages[index].content}|  " + message.content.message
//            val newMessage = Content(message.content.message,imageURL = null, newId, messages[index].creator, MessageType.QUOTED)
//            addMessage(newMessage)
//        }
//    }
//
//    override fun favoriteMessage(message: ChatSimpleOperator) {
//        if (message.messageID in idMessages && message.messageID !in favoriteMessages)
//            favoriteMessages.add(message.messageID)
//    }
//
//    override fun disfavorMessage(message: ChatSimpleOperator) {
//        if (message.messageID in idMessages && message.messageID in favoriteMessages)
//            favoriteMessages.remove(message.messageID)
//    }
}