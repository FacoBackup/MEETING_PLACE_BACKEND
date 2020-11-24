package br.meetingplace.server.modules.chat.dao.delete

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.classes.message.Message
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.global.methods.chat.getContent
import br.meetingplace.server.requests.chat.operators.ChatSimpleOperator

object DeleteMessage {

    fun deleteMessage(data: ChatSimpleOperator, rwUser: UserDBInterface, rwGroup: GroupDBInterface, rwCommunity: CommunityDBInterface, rwChat: ChatDBInterface):Status{
        val user = rwUser.select(data.login.email)
        lateinit var messages: List<Message>

        return if (user != null) {
            when (data.receiver.userGroup || data.receiver.communityGroup) {
                true -> { //GROUP
                    val group = rwGroup.select(data.receiver.receiverID)
                    return if (group != null) {
                        val chat = rwChat.select(group.getChatID())
                        return when (data.receiver.communityGroup) {
                            true -> {
                                val community = rwCommunity.select(group.getOwner().ID)
                                if (community != null && chat != null && group.getID() in community.getGroups()) {
                                    messages = chat.getMessages()
                                    messages.remove(getContent(chat.getMessages(), data.messageID))
                                    chat.setMessages(messages)

                                    rwChat.insert(chat)
                                    Status(statusCode = 200, StatusMessages.OK)
                                }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                            }
                            false -> {
                                return if (chat != null) {
                                    messages = chat.getMessages()
                                    messages.remove(getContent(chat.getMessages(), data.messageID))
                                    chat.setMessages(messages)

                                    rwChat.insert(chat)
                                    Status(statusCode = 200, StatusMessages.OK)
                                } else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                            }
                        }
                    } else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                }
                false -> { //USER <-> USER
                    val chat = rwChat.select(data.receiver.chatID)
                    return if (chat != null) {
                        messages = chat.getMessages()
                        messages.remove(getContent(chat.getMessages(), data.messageID))
                        chat.setMessages(messages)

                       return rwChat.insert(chat)

                    }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                }
            }
        } else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }
}