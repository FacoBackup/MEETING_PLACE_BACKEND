package br.meetingplace.server.modules.chat.dao.send

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.dto.Chat
import br.meetingplace.server.modules.chat.dto.ChatIdentifier
import br.meetingplace.server.modules.chat.dto.dependencies.data.Content
import br.meetingplace.server.modules.chat.dto.dependencies.data.MessageType
import br.meetingplace.server.modules.global.dto.notification.NotificationData
import br.meetingplace.server.modules.global.dto.notification.types.NotificationMainType
import br.meetingplace.server.modules.global.dto.notification.types.NotificationSubType
import br.meetingplace.server.modules.global.dto.owner.OwnerData
import br.meetingplace.server.modules.global.dto.owner.OwnerType
import br.meetingplace.server.requests.chat.data.MessageData
import java.util.*

class SendMessage private constructor() {
    companion object {
        private val Class = SendMessage()
        fun getClass() = Class
    }

    fun sendMessage(data: MessageData, userDB: UserDBInterface, rwGroup: GroupDBInterface, rwCommunity: CommunityDBInterface, rwChat: ChatDBInterface) {
        lateinit var messages: List<Content>

        if (userDB.check(data.login.email) && data.receiver.receiverID != data.login.email) {
            when (data.receiver.userGroup || data.receiver.communityGroup) {
                true -> { //GROUP
                    val group = rwGroup.select(data.receiver.receiverID)
                    if (group != null) {
                        val chat = rwChat.select(group.getChatID())
                        when (data.receiver.communityGroup) {
                            true -> {
                                val community = rwCommunity.select(group.getOwner().ID)
                                if (community != null && chat != null) {
                                    messages = chat.getMessages()
                                    messages.add(Content(data.message, imageURL = data.imageURL, UUID.randomUUID().toString(), data.login.email, data.messageType
                                            ?: MessageType.NORMAL))
                                    chat.setMessages(messages = messages)

                                    rwChat.insert(chat)
                                }
                            }
                            false -> {
                                if (chat != null) {
                                    messages = chat.getMessages()
                                    messages.add(Content(data.message, imageURL = data.imageURL, UUID.randomUUID().toString(), data.login.email, data.messageType
                                            ?: MessageType.NORMAL))
                                    chat.setMessages(messages = messages)

                                    rwChat.insert(chat)
                                }
                            }
                        }
                    }
                }
                false -> { //USER <-> USER
                    val chat = rwChat.select(data.receiver.chatID)
                    if (chat != null) {
                        messages = chat.getMessages()
                        messages.add(Content(data.message, imageURL = data.imageURL, UUID.randomUUID().toString(), data.login.email, data.messageType ?: MessageType.NORMAL))
                        chat.setMessages(messages = messages)

                        rwChat.insert(chat)
                    } else createChat(data, userDB, rwChat)
                }
            }
        }
    }

    private fun createChat(data: MessageData, rwUser: UserDBInterface, rwChat: ChatDBInterface) {
        val logged = data.login.email
        val user = rwUser.select(data.login.email)
        val receiver = rwUser.select(data.receiver.receiverID)

        lateinit var notification: NotificationData
        lateinit var chat: Chat
        lateinit var messages: List<Content>
        lateinit var userChats: List<ChatIdentifier>
        lateinit var receiverChats: List<ChatIdentifier>
        if (user != null && receiver != null) {
            chat = Chat(UUID.randomUUID().toString(), OwnerData(user.getEmail(), OwnerType.USER))
            notification = NotificationData(NotificationMainType.CHAT, NotificationSubType.NEW_MESSAGE, logged)
            messages = chat.getMessages()
            messages.add(Content(data.message, imageURL = data.imageURL, UUID.randomUUID().toString(), logged, data.messageType?: MessageType.NORMAL))
            chat.setMessages(messages = messages)

            userChats = user.getChats()
            userChats.add(ChatIdentifier(chat.getID(), receiver.getEmail()))
            user.setChats(userChats)

            receiverChats = receiver.getChats()
            receiverChats.add(ChatIdentifier(chat.getID(), user.getEmail()))
            receiver.setChats(receiverChats)

            rwChat.insert(chat)
            rwUser.insert(user)
            rwUser.insert(receiver)
        }
    }
}