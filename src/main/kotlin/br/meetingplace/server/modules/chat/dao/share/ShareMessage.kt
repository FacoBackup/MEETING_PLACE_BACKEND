package br.meetingplace.server.modules.chat.dao.share

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.dao.send.SendMessage
import br.meetingplace.server.modules.chat.db.message.MessageType
import br.meetingplace.server.modules.global.methods.chat.getContent
import br.meetingplace.server.requests.chat.data.MessageData
import br.meetingplace.server.requests.chat.operators.ChatComplexOperator

object ShareMessage {
    fun shareMessage(data: ChatComplexOperator, userDB: UserDBInterface, groupDB: GroupDBInterface, communityDB: CommunityDBInterface, chatDB: ChatDBInterface) {
        val user = userDB.select(data.login.email)
        if (user != null) {
            when (data.source.userGroup || data.source.communityGroup) {
                true -> { //GROUP
                    val group = groupDB.select(data.source.receiverID)
                    if (group != null) {
                        val chat = chatDB.select(group.getChatID())
                        when (data.receiver.communityGroup) {
                            true -> {
                                val community = communityDB.select(group.getOwner().ID)
                                if (community != null && chat != null && group.getID() in community.getGroups()) {
                                    val content = getContent(chat.getMessages(), data.messageID)
                                    if (content != null)
                                        SendMessage.getClass().sendMessage(MessageData("|Shared| ${content.content}", imageURL = data.content.imageURL, MessageType.SHARED, data.receiver, data.login), userDB, groupDB, communityDB, chatDB)

                                    chatDB.insert(chat)
                                }
                            }
                            false -> {
                                if (chat != null) {
                                    val content = getContent(chat.getMessages(), data.messageID)
                                    if (content != null)
                                        SendMessage.getClass().sendMessage(MessageData("|Shared| ${content.content}", imageURL = data.content.imageURL, MessageType.SHARED, data.receiver, data.login), userDB, groupDB, communityDB, chatDB)

                                    chatDB.insert(chat)
                                }
                            }
                        }
                    }
                }
                false -> { //USER <-> USER
                    val chat = chatDB.select(data.source.chatID)
                    if (chat != null) {
                        val content = getContent(chat.getMessages(), data.messageID)
                        if (content != null)
                            SendMessage.getClass().sendMessage(MessageData("|Shared| ${content.content}", imageURL = data.content.imageURL, MessageType.SHARED, data.receiver, data.login), userDB, groupDB, communityDB, chatDB)
                        chatDB.insert(chat)
                    }
                }
            }
        }
    }
}