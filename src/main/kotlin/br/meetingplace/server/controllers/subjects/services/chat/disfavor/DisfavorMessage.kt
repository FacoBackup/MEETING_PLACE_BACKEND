package br.meetingplace.server.controllers.subjects.services.chat.disfavor

import br.meetingplace.server.controllers.readwrite.chat.ChatRWInterface
import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.group.GroupRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.chat.ChatSimpleOperator

class DisfavorMessage private constructor() {
    companion object {
        private val Class = DisfavorMessage()
        fun getClass() = Class
    }

    fun disfavorMessage(data: ChatSimpleOperator, rwUser: UserRWInterface, rwGroup: GroupRWInterface, rwCommunity: CommunityRWInterface, rwChat: ChatRWInterface){
        val user = rwUser.read(data.login.email)
        if(user != null){
            when(data.receiver.userGroup || data.receiver.communityGroup){
                true -> { //GROUP
                    val group = rwGroup.read(data.receiver.receiverID)
                    if (group != null) {
                        val chat = rwChat.read(group.getChatID())
                        when (data.receiver.communityGroup) {
                            true -> {
                                val community = rwCommunity.read(group.getOwner().groupOwnerID)
                                if (community != null && chat != null) {
                                    chat.disfavorMessage(data)
                                    rwChat.write(chat)
                                }
                            }
                            false -> {
                                if(chat != null){
                                    chat.disfavorMessage(data)
                                    rwChat.write(chat)
                                }
                            }
                        }
                    }
                }
                false->{ //USER <-> USER
                    val chat = rwChat.read(data.receiver.chatID)
                    if(chat != null){
                        chat.disfavorMessage(data)
                        rwChat.write(chat)
                    }
                }
            }
        }
    }
//    fun userDisfavorMessage(data: ChatSimpleOperator) {
//        val user = rw.readUser(data.login.email)
//        val receiver = rw.readUser(data.receiver.receiverID)
//        val chat = rw.readChat(iDs.getChatId(data.receiver.ownerID, data.receiver.receiverID), data.receiver.ownerID, "", group = false, community = false)
//
//        if (verify.verifyUser(user) && verify.verifyUser(receiver) && verify.verifyChat(chat)) {
//            chat.disfavorMessage(data)
//            rw.writeChat(chat)
//        }
//    }
//
//    fun groupDisfavorMessage(data: ChatSimpleOperator) {
//        when (data.receiver.communityGroup) {
//            false -> {
//                val group = rw.readGroup(data.receiver.receiverID, data.receiver.ownerID, false)
//                val user = rw.readUser(data.login.email)
//
//                if (verify.verifyUser(user) && verify.verifyGroup(group) && group.verifyMember(user.getEmail())) {
//                    val chat = rw.readChat("", user.getEmail(), group.getGroupID(), group = true, false)
//                    chat.disfavorMessage(data)
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
//                    chat.disfavorMessage(data)
//                    rw.writeChat(chat)
//                }
//            }
//        }
//    }
}