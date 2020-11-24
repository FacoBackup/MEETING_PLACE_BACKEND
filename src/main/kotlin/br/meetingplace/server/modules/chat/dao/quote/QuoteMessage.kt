package br.meetingplace.server.modules.chat.dao.quote

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.classes.message.Message
import br.meetingplace.server.modules.chat.classes.message.MessageType
import br.meetingplace.server.modules.global.methods.chat.getContent
import br.meetingplace.server.requests.chat.operators.ChatComplexOperator
import java.util.*

object QuoteMessage {

    fun quoteMessage(data: ChatComplexOperator, rwUser: UserDBInterface, rwGroup: GroupDBInterface, rwCommunity: CommunityDBInterface, rwChat: ChatDBInterface) {
        val user = rwUser.select(data.login.email)
        lateinit var messages: List<Message>

        if (user != null) {
            when (data.receiver.userGroup || data.receiver.communityGroup) {
                true -> { //GROUP
                    val group = rwGroup.select(data.receiver.receiverID)
                    if (group != null) {
                        val chat = rwChat.select(group.getChatID())
                        when (data.receiver.communityGroup) {
                            true -> {
                                val community = rwCommunity.select(group.getOwner().ID)
                                if (community != null && chat != null && group.getID() in community.getGroups()) {

                                    messages = chat.getMessages()
                                    val toBeQuoted = getContent(messages, data.messageID)
                                    if (toBeQuoted != null && toBeQuoted.content.isNotBlank()) {
                                        messages.add(Message(content = toBeQuoted.content.plus(data.content), imageURL = data.content.imageURL, ID = UUID.randomUUID().toString(), creator = user.getEmail(), MessageType.QUOTED))
                                        chat.setMessages(messages = messages)
                                    }

                                    rwChat.insert(chat)
                                }
                            }
                            false -> {
                                if (chat != null) {
                                    messages = chat.getMessages()
                                    val toBeQuoted = getContent(messages, data.messageID)
                                    if (toBeQuoted != null && toBeQuoted.content.isNotBlank()) {
                                        messages.add(Message(content = toBeQuoted.content.plus(data.content), imageURL = data.content.imageURL, ID = UUID.randomUUID().toString(), creator = user.getEmail(), MessageType.QUOTED))
                                        chat.setMessages(messages = messages)
                                    }
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
                        val toBeQuoted = getContent(messages, data.messageID)
                        if (toBeQuoted != null && toBeQuoted.content.isNotBlank()) {
                            messages.add(Message(content = toBeQuoted.content.plus(data.content), imageURL = data.content.imageURL, ID = UUID.randomUUID().toString(), creator = user.getEmail(), MessageType.QUOTED))
                            chat.setMessages(messages = messages)
                        }
                        rwChat.insert(chat)
                    }
                }
            }
        }
    }
}