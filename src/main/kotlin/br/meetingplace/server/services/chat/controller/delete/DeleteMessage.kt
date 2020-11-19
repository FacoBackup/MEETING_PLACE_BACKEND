package br.meetingplace.server.services.chat.controller.delete

import br.meetingplace.server.loadstore.interfaces.ChatLSInterface
import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.GroupLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.chat.ChatSimpleOperator

class DeleteMessage private constructor() {
    companion object {
        private val Class = DeleteMessage()
        fun getClass() = Class
    }

    fun deleteMessage(data: ChatSimpleOperator, rwUser: UserLSInterface, rwGroup: GroupLSInterface, rwCommunity: CommunityLSInterface, rwChat: ChatLSInterface){
        val user = rwUser.load(data.login.email)
        if(user != null){
            when(data.receiver.userGroup || data.receiver.communityGroup){
                true->{ //GROUP
                    val group = rwGroup.read(data.receiver.receiverID)
                    if(group != null){
                        val chat = rwChat.load(group.getChatID())
                        when(data.receiver.communityGroup){
                            true->{
                                val community = rwCommunity.load(group.getOwner().groupOwnerID)
                                if(community != null && chat != null) {
                                    chat.deleteMessage(data)
                                    rwChat.store(chat)
                                }

                            }
                            false-> if(chat!= null){
                                        chat.deleteMessage(data)
                                        rwChat.store(chat)
                                    }
                        }
                    }
                }
                false->{ //USER <-> USER
                    val chat = rwChat.load(data.receiver.chatID)
                    if(chat!= null){
                        chat.deleteMessage(data)
                        rwChat.store(chat)
                    }
                }
            }
        }
    }
//
//    fun deleteUserMessage(data: ChatSimpleOperator) {
//        val user = rw.readUser(data.login.email)
//        val receiver = rw.readUser(data.receiver.receiverID)
//        val chat = rw.readChat(getChatID(data.receiver.mainOwnerID, data.receiver.receiverID), data.receiver.mainOwnerID, "", false, community = false)
//
//        if (user != null && receiver != null  && chat != null) {
//            chat.deleteMessage(data)
//            rw.writeChat(chat)
//            rw.writeUser(user, user.getEmail())
//            rw.writeUser(receiver, receiver.getEmail())
//        }
//    }//DELETE
//
//
//    fun deleteGroupMessage(data: ChatSimpleOperator) {
//        when (data.receiver.communityGroup) {
//            false -> userDeleteGroupMessage(data)
//            true -> communityDeleteGroupMessage(data)
//        }
//    }
//
//    private fun communityDeleteGroupMessage(data: ChatSimpleOperator) {
//        val user = rw.readUser(data.login.email)
//        val community = rw.readCommunity(data.receiver.mainOwnerID)
//        val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, community = true)
//        val chat = rw.readChat(group.getChatID())
//
//        if (user != null && group != null  && community != null &&
//                community.checkGroupApproval(group.getGroupID()) && chat.verifyMessage(data.messageID)) {
//
//            chat.deleteMessage(data)
//            rw.writeChat(chat)
//            rw.writeGroup(group)
//        }
//    }
//
//    private fun userDeleteGroupMessage(data: ChatSimpleOperator) {
//        val user = rw.readUser(data.login.email)
//        val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, community = false)
//        val chat = rw.readChat(group.getChatID())
//
//        if (user != null && group != null && chat.verifyMessage(data.messageID)) {
//            chat.deleteMessage(data)
//            rw.writeChat(chat)
//            rw.writeGroup(group)
//        }
//    }
}