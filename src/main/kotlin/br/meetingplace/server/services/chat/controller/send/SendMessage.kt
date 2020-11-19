package br.meetingplace.server.services.chat.controller.send

import br.meetingplace.server.loadstore.interfaces.ChatLSInterface
import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.GroupLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.chat.MessageData
import br.meetingplace.server.services.chat.classes.Chat
import br.meetingplace.server.services.chat.classes.SimplifiedChat
import br.meetingplace.server.services.chat.classes.dependencies.data.MessageContent
import br.meetingplace.server.services.chat.classes.dependencies.data.MessageType
import br.meetingplace.server.data.classes.notification.NotificationData
import br.meetingplace.server.data.classes.notification.data.NotificationMainType
import br.meetingplace.server.data.classes.notification.data.NotificationSubType
import br.meetingplace.server.data.classes.owner.OwnerType
import br.meetingplace.server.data.classes.owner.chat.ChatOwnerData
import java.util.*

class SendMessage private constructor() {
    companion object {
        private val Class = SendMessage()
        fun getClass() = Class
    }
    fun sendMessage(data: MessageData, rwUser: UserLSInterface, rwGroup: GroupLSInterface, rwCommunity: CommunityLSInterface, rwChat: ChatLSInterface){
        val user = rwUser.load(data.login.email)
        if(user != null && data.receiver.receiverID != data.login.email){
            when(data.receiver.userGroup || data.receiver.communityGroup){
                true -> { //GROUP
                    val group = rwGroup.read(data.receiver.receiverID)
                    if (group != null) {
                        val chat = rwChat.load(group.getChatID())
                        when (data.receiver.communityGroup) {
                            true -> {
                                val community = rwCommunity.load(group.getOwner().groupOwnerID)
                                if (community != null && chat != null) {
                                    chat.addMessage(MessageContent(data.message, UUID.randomUUID().toString(), user.getEmail(), data.messageType ?: MessageType.NORMAL))
                                    rwChat.store(chat)
                                }
                            }
                            false -> {
                                if(chat != null){
                                    chat.addMessage(MessageContent(data.message, UUID.randomUUID().toString(), user.getEmail(), data.messageType ?: MessageType.NORMAL))
                                    rwChat.store(chat)
                                }
                            }
                        }
                    }
                }
                false->{ //USER <-> USER
                    val chat = rwChat.load(data.receiver.chatID)
                    if(chat != null){
                        chat.addMessage(MessageContent(data.message, UUID.randomUUID().toString(), user.getEmail(), data.messageType ?: MessageType.NORMAL))
                        rwChat.store(chat)
                    }
                    else createChat(data, rwUser, rwChat)
                }
            }
        }
    }

    private fun createChat(data: MessageData, rwUser: UserLSInterface, rwChat: ChatLSInterface) {
        val logged = data.login.email
        val user = rwUser.load(data.login.email)
        val receiver = rwUser.load(data.receiver.receiverID)

        lateinit var messageContent: MessageContent
        lateinit var notification: NotificationData
        lateinit var chat: Chat

        if (user != null && receiver!= null) {
            chat = Chat(UUID.randomUUID().toString(), ChatOwnerData(user.getEmail(), receiver.getEmail(), OwnerType.USER, OwnerType.USER))
            notification = NotificationData(NotificationMainType.CHAT, NotificationSubType.NEW_MESSAGE)

            messageContent = MessageContent(data.message, UUID.randomUUID().toString(), logged, data.messageType ?: MessageType.NORMAL)
            chat.addMessage(messageContent)


            user.updateMyChats(SimplifiedChat(chat.getID(), receiver.getEmail()))
            receiver.updateMyChats(SimplifiedChat(chat.getID(), user.getEmail()))
            receiver.updateInbox(notification)
            println ("HEre ")
            rwChat.store(chat)
            rwUser.store(user)
            rwUser.store(receiver)
        }
    }

//    fun sendUserMessage(data: MessageData) {
//        val user = rw.readUser(data.login.email)
//        val receiver = rw.readUser(data.receiver.receiverID)
//        lateinit var simplifiedChat: SimplifiedChat
//        lateinit var msg: MessageContent
//        lateinit var notification: NotificationData
//
//        if (verify.verifyUser(user) && verify.verifyUser(receiver)) {
//            val chat = rw.readChat(iDs.getChatId(data.receiver.ownerID, data.receiver.receiverID), data.receiver.ownerID, "", group = false, community = false)
//            when (verify.verifyChat(chat)) {
//                true -> { //The chat exists
//                    msg = MessageContent(data.message, iDs.generateId(), data.login.email, data.messageType
//                            ?: MessageType.NORMAL)
//                    notification = NotificationData("${user.getUserName()} sent a new message.", "Message.")
//                    chat.addMessage(msg)
//                    rw.writeChat(chat)
//
//                    simplifiedChat = SimplifiedChat(chat.getOwner(), chat.getID())
//                    receiver.updateMyChats(simplifiedChat)
//                    receiver.updateInbox(notification)
//                    user.updateMyChats(simplifiedChat)
//
//                    rw.writeUser(user, user.getEmail())
//                    rw.writeUser(receiver, receiver.getEmail())
//
//                }
//                false -> ChatFactory.getClass().createChat(data) //The chat doesn't exist
//            }
//        }
//    }
//
//    fun sendGroupMessage(data: MessageData) {
//        when (data.receiver.communityGroup) {
//            false -> {
//                val group = rw.readGroup(data.receiver.receiverID, data.receiver.ownerID, community = false)
//                val user = rw.readUser(data.login.email)
//                if (verify.verifyUser(user) && verify.verifyGroup(group)) {
//                    println("step 1")
//                    sendMessage(data.message,
//                            user,
//                            data.login.email,
//                            group,
//                            rw.readChat("", data.receiver.ownerID, group.getGroupID(), true, community = false))
//                }
//
//            }
//            true -> {
//                val community = rw.readCommunity(data.receiver.ownerID)
//                val group = rw.readGroup(data.receiver.receiverID, data.receiver.ownerID, community = true)
//                val user = rw.readUser(data.login.email)
//
//                if (verify.verifyUser(user) && verify.verifyCommunity(community) && verify.verifyGroup(group) && community.checkGroupApproval(group.getGroupID())) {
//
//                    sendMessage(data.message,
//                            user,
//                            data.login.email,
//                            group,
//                            rw.readChat("", community.getID(), group.getGroupID(), group = true, community = true))
//                }
//
//            }
//        }
//    }
//
//    private fun sendMessage(message: String, user: User, logged: String, group: Group, chat: Chat) {
//        println("step 2")
//        val messageContent = MessageContent(message, iDs.generateId(), logged, MessageType.NORMAL)
//        val notification = NotificationData("${user.getUserName()} from ${group.getGroupID()} sent a new message.", Type = "Group Message.")
//        val groupMembers = group.getMembers()
//        chat.addMessage(messageContent)
//        rw.writeChat(chat)
//        println("done")
//        for (i in groupMembers.indices) {
//            val member = rw.readUser(groupMembers[i])
//            if (verify.verifyUser(member) && groupMembers[i] != logged) {
//                member.updateInbox(notification)
//                rw.writeUser(member, member.getEmail())
//            }
//        }
//
//        val creator = rw.readUser(group.getCreator())
//        if (logged != group.getCreator() && verify.verifyUser(creator)) {
//            creator.updateInbox(notification)
//            rw.writeUser(creator, creator.getEmail())
//        }
//
//    }
}