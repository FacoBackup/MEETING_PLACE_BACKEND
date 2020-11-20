package br.meetingplace.server.modules.chat.dao.share

import br.meetingplace.server.db.interfaces.ChatDBInterface
import br.meetingplace.server.db.interfaces.CommunityDBInterface
import br.meetingplace.server.db.interfaces.GroupDBInterface
import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.modules.chat.dao.send.SendMessage
import br.meetingplace.server.modules.chat.dto.dependencies.data.MessageType
import br.meetingplace.server.routers.chat.requests.ChatComplexOperator
import br.meetingplace.server.routers.chat.requests.MessageData

class ShareMessage private constructor() {
    companion object {
        private val Class = ShareMessage()
        fun getClass() = Class
    }

    fun shareMessage(data: ChatComplexOperator, rwUser: UserDBInterface, rwGroup: GroupDBInterface, rwCommunity: CommunityDBInterface, rwChat: ChatDBInterface){
        val user = rwUser.select(data.login.email)
        if(user != null){
            when(data.source.userGroup || data.source.communityGroup){
                true -> { //GROUP
                    val group = rwGroup.select(data.source.receiverID)
                    if (group != null) {
                        val chat = rwChat.select(group.getChatID())
                        when (data.receiver.communityGroup) {
                            true -> {
                                val community = rwCommunity.select(group.getOwner().groupOwnerID)
                                if (community != null && chat != null) {
                                    val messageContent = chat.shareMessage(data)
                                    SendMessage.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login), rwUser, rwGroup, rwCommunity, rwChat)
                                    rwChat.insert(chat)
                                }
                            }
                            false -> {
                                if(chat != null){
                                    val messageContent = chat.shareMessage(data)
                                    SendMessage.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login), rwUser, rwGroup, rwCommunity, rwChat)
                                    rwChat.insert(chat)
                                }
                            }
                        }
                    }
                }
                false->{ //USER <-> USER
                    val chat = rwChat.select(data.source.chatID)
                    if(chat != null){
                        val messageContent = chat.shareMessage(data)
                        SendMessage.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login), rwUser, rwGroup, rwCommunity, rwChat)
                        rwChat.insert(chat)
                    }
                }
            }
        }
    }

//
//    fun userShareMessage(data: ChatComplexOperator) {
//        val user = rw.readUser(data.login.email)
//        val receiver = rw.readUser(data.receiver.receiverID)
//
//        if (verify.verifyUser(user) && verify.verifyUser(receiver)) {
//            val chat = rw.readChat(iDs.getChatId(data.source.ownerID, data.source.receiverID), data.source.ownerID, "", false, community = false)
//            if (verify.verifyChat(chat)) {
//                val message = chat.shareMessage(data)
//                if (!message.isNullOrBlank())
//                    _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController.getClass().sendMessage(MessageData("|Shared| ${data.message}", MessageType.SHARED, data.receiver, data.login))
//            }
//        }
//    }
//
//    fun groupShareMessage(data: ChatComplexOperator) {
//        when (data.receiver.communityGroup) {
//            false -> {
//                val group = rw.readGroup(data.source.receiverID, data.source.ownerID, false)
//                val user = rw.readUser(data.login.email)
//
//                if (verify.verifyUser(user) && verify.verifyGroup(group) && group.verifyMember(user.getEmail())) {
//                    val chat = rw.readChat("", user.getEmail(), group.getGroupID(), group = true, false)
//                    val messageContent = chat.shareMessage(data)
//
//                    if (!messageContent.isNullOrBlank())
//                        _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login))
//                }
//            }
//            true -> {
//                val group = rw.readGroup(data.source.receiverID, data.source.ownerID, true)
//                val user = rw.readUser(data.login.email)
//                val community = rw.readCommunity(data.source.ownerID)
//
//                if (verify.verifyUser(user) && verify.verifyGroup(group) && verify.verifyCommunity(community)
//                        && community.checkGroupApproval(group.getGroupID()) && group.verifyMember(user.getEmail())) {
//                    val chat = rw.readChat("", community.getID(), group.getGroupID(), group = true, true)
//                    val messageContent = chat.shareMessage(data)
//
//                    if (!messageContent.isNullOrBlank())
//                        _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login))
//                }
//            }
//        }
//    }
}