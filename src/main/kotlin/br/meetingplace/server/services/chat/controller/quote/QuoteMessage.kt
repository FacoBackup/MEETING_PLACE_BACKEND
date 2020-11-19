package br.meetingplace.server.services.chat.controller.quote

import br.meetingplace.server.loadstore.interfaces.ChatLSInterface
import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.GroupLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.chat.ChatComplexOperator
import java.util.*

class QuoteMessage private constructor() {
    companion object {
        private val Class = QuoteMessage()
        fun getClass() = Class
    }

    fun quoteMessage(data: ChatComplexOperator, rwUser: UserLSInterface, rwGroup: GroupLSInterface, rwCommunity: CommunityLSInterface, rwChat: ChatLSInterface){
        val user = rwUser.load(data.login.email)
        if(user != null){
            when(data.receiver.userGroup || data.receiver.communityGroup){
                true -> { //GROUP
                    val group = rwGroup.read(data.receiver.receiverID)
                    if (group != null) {
                        val chat = rwChat.load(group.getChatID())
                        when (data.receiver.communityGroup) {
                            true -> {
                                val community = rwCommunity.load(group.getOwner().groupOwnerID)
                                if (community != null && chat != null) {
                                    chat.quoteMessage(data, UUID.randomUUID().toString())
                                    rwChat.store(chat)
                                }
                            }
                            false -> {
                                if(chat != null){
                                    chat.quoteMessage(data, UUID.randomUUID().toString())
                                    rwChat.store(chat)
                                }
                            }
                        }
                    }
                }
                false->{ //USER <-> USER
                    val chat = rwChat.load(data.receiver.chatID)
                    if(chat != null){
                        chat.quoteMessage(data, UUID.randomUUID().toString())
                        rwChat.store(chat)
                    }
                }
            }
        }
    }

//
//    fun userQuoteMessage(data: ChatComplexOperator) {
//        val user = rw.readUser(data.login.email)
//        val receiver = rw.readUser(data.receiver.receiverID)
//        lateinit var notification: NotificationData
//
//        if (verify.verifyUser(user) && verify.verifyUser(receiver)) {
//            val chat = rw.readChat(iDs.getChatId(data.receiver.ownerID, data.receiver.receiverID), data.receiver.ownerID, "", false, community = false)
//            if (verify.verifyChat(chat)) {
//                notification = NotificationData("${user.getUserName()} sent a new message.", "Message.")
//                chat.quoteMessage(data, iDs.generateId())
//                rw.writeChat(chat)
//                receiver.updateInbox(notification)
//                rw.writeUser(receiver, receiver.getEmail())
//            }
//
//        }
//    }
//
//    fun groupQuoteMessage(data: ChatComplexOperator) {
//        when (data.receiver.communityGroup) {
//            false -> {
//                val group = rw.readGroup(data.receiver.receiverID, data.receiver.ownerID, false)
//                val user = rw.readUser(data.login.email)
//
//                if (verify.verifyUser(user) && verify.verifyGroup(group) && group.verifyMember(user.getEmail())) {
//                    val chat = rw.readChat("", user.getEmail(), group.getGroupID(), group = true, false)
//                    chat.quoteMessage(data, iDs.generateId())
//                    rw.writeChat(chat)
//                }
//            }
//            true -> {
//                val group = rw.readGroup(data.receiver.receiverID, data.receiver.ownerID, true)
//                val user = rw.readUser(data.login.email)
//                val community = rw.readCommunity(data.receiver.ownerID)
//
//                if (verify.verifyUser(user) && verify.verifyGroup(group) && verify.verifyCommunity(community)
//                        && community.checkGroupApproval(group.getGroupID()) && group.verifyMember(user.getEmail())) {
//                    val chat = rw.readChat("", community.getID(), group.getGroupID(), group = true, true)
//                    chat.quoteMessage(data, iDs.generateId())
//                    rw.writeChat(chat)
//                }
//            }
//        }
//    }
}