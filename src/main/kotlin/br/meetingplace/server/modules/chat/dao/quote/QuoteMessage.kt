package br.meetingplace.server.modules.chat.dao.quote

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.dto.dependencies.data.Content
import br.meetingplace.server.modules.chat.dto.dependencies.data.MessageType
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.methods.chat.getContent
import br.meetingplace.server.requests.chat.operators.ChatComplexOperator
import java.util.*

class QuoteMessage private constructor() {
    companion object {
        private val Class = QuoteMessage()
        fun getClass() = Class
    }

    fun quoteMessage(data: ChatComplexOperator, rwUser: UserDBInterface, rwGroup: GroupDBInterface, rwCommunity: CommunityDBInterface, rwChat: ChatDBInterface): Status {
        val user = rwUser.select(data.login.email)
        lateinit var messages: List<Content>

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
                                        messages.add(Content(content = toBeQuoted.content.plus(data.content), imageURL = data.content.imageURL, ID = UUID.randomUUID().toString(), creator = user.getEmail(), MessageType.QUOTED))
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
                                        messages.add(Content(content = toBeQuoted.content.plus(data.content), imageURL = data.content.imageURL, ID = UUID.randomUUID().toString(), creator = user.getEmail(), MessageType.QUOTED))
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
                            messages.add(Content(content = toBeQuoted.content.plus(data.content), imageURL = data.content.imageURL, ID = UUID.randomUUID().toString(), creator = user.getEmail(), MessageType.QUOTED))
                            chat.setMessages(messages = messages)
                        }
                        rwChat.insert(chat)
                    }
                }
            }
        }
    }
}