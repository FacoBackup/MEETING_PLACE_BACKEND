package br.meetingplace.server.modules.chat.dao.send

import br.meetingplace.server.modules.chat.db.Chat
import br.meetingplace.server.modules.chat.db.ChatOwner
import br.meetingplace.server.modules.chat.dto.MessageDTO
import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.modules.global.http.status.StatusMessages
import br.meetingplace.server.requests.chat.data.MessageData
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object SendMessage {

    fun sendMessage(data: MessageData): Status{
        return try {

                val chatOwner = transaction {ChatOwner.select{ChatOwner.receiverID eq data.receiverID}}
                if(chatOwner.){
                    val newMessage = MessageDTO(content = data.message, imageURL = data.imageURL, UUID.randomUUID().toString(), creatorID = data.userID, type = 0, chatID = )


                    Status(200, StatusMessages.OK)
                }
                else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)


        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

//
//    fun sendMessage(data: MessageData, userDB: UserDBInterface, rwGroup: GroupDBInterface, rwCommunity: CommunityDBInterface, rwChat: ChatDBInterface) {
//        lateinit var messages: List<MessageDTO>
//
//        if (userDB.check(data.login.email) && data.receiver.receiverID != data.login.email) {
//            when (data.receiver.userGroup || data.receiver.communityGroup) {
//                true -> { //GROUP
//                    val group = rwGroup.select(data.receiver.receiverID)
//                    if (group != null) {
//                        val chat = rwChat.select(group.getChatID())
//                        when (data.receiver.communityGroup) {
//                            true -> {
//                                val community = rwCommunity.select(group.getOwner().ID)
//                                if (community != null && chat != null) {
//                                    messages = chat.getMessages()
//                                    messages.add(
//                                        MessageDTO(data.message, imageURL = data.imageURL, UUID.randomUUID().toString(), data.login.email, data.messageType
//                                            ?: MessageType.NORMAL)
//                                    )
//                                    chat.setMessages(messages = messages)
//
//                                    rwChat.insert(chat)
//                                }
//                            }
//                            false -> {
//                                if (chat != null) {
//                                    messages = chat.getMessages()
//                                    messages.add(
//                                        MessageDTO(data.message, imageURL = data.imageURL, UUID.randomUUID().toString(), data.login.email, data.messageType
//                                            ?: MessageType.NORMAL)
//                                    )
//                                    chat.setMessages(messages = messages)
//
//                                    rwChat.insert(chat)
//                                }
//                            }
//                        }
//                    }
//                }
//                false -> { //USER <-> USER
//                    val chat = rwChat.select(data.receiver.chatID)
//                    if (chat != null) {
//                        messages = chat.getMessages()
//                        messages.add(MessageDTO(data.message, imageURL = data.imageURL, UUID.randomUUID().toString(), data.login.email, data.messageType ?: MessageType.NORMAL))
//                        chat.setMessages(messages = messages)
//
//                        rwChat.insert(chat)
//                    } else createChat(data, userDB, rwChat)
//                }
//            }
//        }
//    }
//
//    private fun createChat(data: MessageData, rwUser: UserDBInterface, rwChat: ChatDBInterface) {
//        val logged = data.login.email
//        val user = rwUser.select(data.login.email)
//        val receiver = rwUser.select(data.receiver.receiverID)
//
//        lateinit var notification: NotificationDTO
//        lateinit var chat: Chat
//        lateinit var messages: List<MessageDTO>
//        lateinit var userChats: List<ChatIdentifier>
//        lateinit var receiverChats: List<ChatIdentifier>
//        lateinit var receiverNotifications: List<NotificationDTO>
//        if (user != null && receiver != null) {
//            chat = Chat(UUID.randomUUID().toString(), OwnerData(user.getEmail(), OwnerType.USER))
//            notification = NotificationDTO(NotificationMainType.CHAT, NotificationSubType.NEW_MESSAGE, logged)
//            messages = chat.getMessages()
//            messages.add(MessageDTO(data.message, imageURL = data.imageURL, UUID.randomUUID().toString(), logged, data.messageType?: MessageType.NORMAL))
//            chat.setMessages(messages = messages)
//
//            userChats = user.getChats()
//            userChats.add(ChatIdentifier(chat.getID(), receiver.getEmail()))
//            user.setChats(userChats)
//
//            receiverChats = receiver.getChats()
//            receiverChats.add(ChatIdentifier(chat.getID(), user.getEmail()))
//            receiver.setChats(receiverChats)
//
//            receiverNotifications = receiver.getInbox()
//            receiverNotifications.add(notification)
//            receiver.setInbox(receiverNotifications)
//
//            rwChat.insert(chat)
//            rwUser.insert(user)
//            rwUser.insert(receiver)
//        }
//    }
}