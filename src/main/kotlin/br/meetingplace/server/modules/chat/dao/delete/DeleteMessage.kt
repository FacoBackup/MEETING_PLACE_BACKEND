package br.meetingplace.server.modules.chat.dao.delete

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.dto.dependencies.data.Content
import br.meetingplace.server.modules.global.functions.chat.getContent
import br.meetingplace.server.requests.chat.operators.ChatSimpleOperator

class DeleteMessage private constructor() {
    companion object {
        private val Class = DeleteMessage()
        fun getClass() = Class
    }

    fun deleteMessage(data: ChatSimpleOperator, rwUser: UserDBInterface, rwGroup: GroupDBInterface, rwCommunity: CommunityDBInterface, rwChat: ChatDBInterface) {
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
                                if (community != null && chat != null && group.getGroupID() in community.getGroups()) {
                                    messages = chat.getMessages()
                                    messages.remove(getContent(chat.getMessages(), data.messageID))
                                    chat.setMessages(messages)

                                    rwChat.insert(chat)
                                }
                            }
                            false -> if (chat != null) {
                                messages = chat.getMessages()
                                messages.remove(getContent(chat.getMessages(), data.messageID))
                                chat.setMessages(messages)

                                rwChat.insert(chat)
                            }
                        }
                    }
                }
                false -> { //USER <-> USER
                    val chat = rwChat.select(data.receiver.chatID)
                    if (chat != null) {
                        messages = chat.getMessages()
                        messages.remove(getContent(chat.getMessages(), data.messageID))
                        chat.setMessages(messages)

                        rwChat.insert(chat)
                    }
                }
            }
        }
    }
}