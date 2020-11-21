package br.meetingplace.server.modules.chat.dao.send

import br.meetingplace.server.modules.notification.dto.NotificationData
import br.meetingplace.server.modules.notification.dto.types.NotificationMainType
import br.meetingplace.server.modules.notification.dto.types.NotificationSubType
import br.meetingplace.server.modules.owner.dto.OwnerType
import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.dto.Chat
import br.meetingplace.server.modules.owner.dto.OwnerData
import br.meetingplace.server.modules.chat.dto.SimplifiedChat
import br.meetingplace.server.modules.chat.dto.dependencies.data.Content
import br.meetingplace.server.modules.chat.dto.dependencies.data.MessageType
import br.meetingplace.server.requests.chat.data.MessageData
import java.util.*

class SendMessage private constructor() {
    companion object {
        private val Class = SendMessage()
        fun getClass() = Class
    }

    fun sendMessage(data: MessageData, rwUser: UserDBInterface, rwGroup: GroupDBInterface, rwCommunity: CommunityDBInterface, rwChat: ChatDBInterface) {
        val user = rwUser.select(data.login.email)
        if (user != null && data.receiver.receiverID != data.login.email) {
            when (data.receiver.userGroup || data.receiver.communityGroup) {
                true -> { //GROUP
                    val group = rwGroup.select(data.receiver.receiverID)
                    if (group != null) {
                        val chat = rwChat.select(group.getChatID())
                        when (data.receiver.communityGroup) {
                            true -> {
                                val community = rwCommunity.select(group.getOwner().groupOwnerID)
                                if (community != null && chat != null) {
                                    chat.addMessage(Content(data.message, imageURL = data.imageURL, UUID.randomUUID().toString(), user.getEmail(), data.messageType
                                            ?: MessageType.NORMAL))
                                    rwChat.insert(chat)
                                }
                            }
                            false -> {
                                if (chat != null) {
                                    chat.addMessage(Content(data.message,imageURL = data.imageURL , UUID.randomUUID().toString(), user.getEmail(), data.messageType
                                            ?: MessageType.NORMAL))
                                    rwChat.insert(chat)
                                }
                            }
                        }
                    }
                }
                false -> { //USER <-> USER
                    val chat = rwChat.select(data.receiver.chatID)
                    if (chat != null) {
                        chat.addMessage(Content(data.message, imageURL = data.imageURL, UUID.randomUUID().toString(), user.getEmail(), data.messageType
                                ?: MessageType.NORMAL))
                        rwChat.insert(chat)
                    } else createChat(data, rwUser, rwChat)
                }
            }
        }
    }

    private fun createChat(data: MessageData, rwUser: UserDBInterface, rwChat: ChatDBInterface) {
        val logged = data.login.email
        val user = rwUser.select(data.login.email)
        val receiver = rwUser.select(data.receiver.receiverID)

        lateinit var messageContent: Content
        lateinit var notification: NotificationData
        lateinit var chat: Chat

        if (user != null && receiver != null) {
            chat = Chat(UUID.randomUUID().toString(), OwnerData(user.getEmail(), receiver.getEmail(), OwnerType.USER, OwnerType.USER))
            notification = NotificationData(NotificationMainType.CHAT, NotificationSubType.NEW_MESSAGE, logged)

            messageContent = Content(data.message,imageURL = data.imageURL, UUID.randomUUID().toString(), logged, data.messageType
                    ?: MessageType.NORMAL)
            chat.addMessage(messageContent)


            user.updateMyChats(SimplifiedChat(chat.getID(), receiver.getEmail()))
            receiver.updateMyChats(SimplifiedChat(chat.getID(), user.getEmail()))
            receiver.updateInbox(notification)
            rwChat.insert(chat)
            rwUser.insert(user)
            rwUser.insert(receiver)
        }
    }
}