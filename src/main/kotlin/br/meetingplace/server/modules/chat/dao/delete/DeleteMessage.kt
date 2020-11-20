package br.meetingplace.server.modules.chat.dao.delete

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.requests.chat.operators.ChatSimpleOperator

class DeleteMessage private constructor() {
    companion object {
        private val Class = DeleteMessage()
        fun getClass() = Class
    }

    fun deleteMessage(data: ChatSimpleOperator, rwUser: UserDBInterface, rwGroup: GroupDBInterface, rwCommunity: CommunityDBInterface, rwChat: ChatDBInterface){
        val user = rwUser.select(data.login.email)
        if(user != null){
            when(data.receiver.userGroup || data.receiver.communityGroup){
                true->{ //GROUP
                    val group = rwGroup.select(data.receiver.receiverID)
                    if(group != null){
                        val chat = rwChat.select(group.getChatID())
                        when(data.receiver.communityGroup){
                            true->{
                                val community = rwCommunity.select(group.getOwner().groupOwnerID)
                                if(community != null && chat != null) {
                                    chat.deleteMessage(data)
                                    rwChat.insert(chat)
                                }

                            }
                            false-> if(chat!= null){
                                        chat.deleteMessage(data)
                                        rwChat.insert(chat)
                                    }
                        }
                    }
                }
                false->{ //USER <-> USER
                    val chat = rwChat.select(data.receiver.chatID)
                    if(chat!= null){
                        chat.deleteMessage(data)
                        rwChat.insert(chat)
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