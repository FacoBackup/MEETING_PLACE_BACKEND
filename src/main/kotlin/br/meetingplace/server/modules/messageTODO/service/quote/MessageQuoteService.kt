package br.meetingplace.server.modules.messageTODO.service.quote

import br.meetingplace.server.request.dto.message.ConversationMessageDTO

object MessageQuoteService {

    fun quoteMessage(data: ConversationMessageDTO) {
        TODO("NOT YET IMPLEMENTED")
//        val user = rwUser.select(data.login.email)
//        lateinit var messages: List<MessageDTO>
//
//        if (user != null) {
//            when (data.receiver.userGroup || data.receiver.communityGroup) {
//                true -> { //GROUP
//                    val group = rwGroup.select(data.receiver.receiverID)
//                    if (group != null) {
//                        val chat = rwChat.select(group.getChatID())
//                        when (data.receiver.communityGroup) {
//                            true -> {
//                                val community = rwCommunity.select(group.getOwner().ID)
//                                if (community != null && chat != null && group.getID() in community.getGroups()) {
//
//                                    messages = chat.getMessages()
//                                    val toBeQuoted = getContent(messages, data.messageID)
//                                    if (toBeQuoted != null && toBeQuoted.content.isNotBlank()) {
//                                        messages.add(MessageDTO(content = toBeQuoted.content.plus(data.content), imageURL = data.content.imageURL, ID = UUID.randomUUID().toString(), creator = user.getEmail(), MessageType.QUOTED))
//                                        chat.setMessages(messages = messages)
//                                    }
//
//                                    rwChat.insert(chat)
//                                }
//                            }
//                            false -> {
//                                if (chat != null) {
//                                    messages = chat.getMessages()
//                                    val toBeQuoted = getContent(messages, data.messageID)
//                                    if (toBeQuoted != null && toBeQuoted.content.isNotBlank()) {
//                                        messages.add(MessageDTO(content = toBeQuoted.content.plus(data.content), imageURL = data.content.imageURL, ID = UUID.randomUUID().toString(), creator = user.getEmail(), MessageType.QUOTED))
//                                        chat.setMessages(messages = messages)
//                                    }
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
//                        val toBeQuoted = getContent(messages, data.messageID)
//                        if (toBeQuoted != null && toBeQuoted.content.isNotBlank()) {
//                            messages.add(MessageDTO(content = toBeQuoted.content.plus(data.content), imageURL = data.content.imageURL, ID = UUID.randomUUID().toString(), creator = user.getEmail(), MessageType.QUOTED))
//                            chat.setMessages(messages = messages)
//                        }
//                        rwChat.insert(chat)
//                    }
//                }
//            }
//        }
    }
}