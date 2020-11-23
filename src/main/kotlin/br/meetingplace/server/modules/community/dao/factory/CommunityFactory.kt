package br.meetingplace.server.modules.community.dao.factory

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.community.dto.Community
import br.meetingplace.server.modules.global.dto.notification.NotificationData
import br.meetingplace.server.modules.global.dto.notification.types.NotificationMainType
import br.meetingplace.server.modules.global.dto.notification.types.NotificationSubType
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.requests.generic.data.CreationData
import br.meetingplace.server.requests.generic.operators.MemberOperator
import java.util.*

class CommunityFactory private constructor() {

    companion object {
        private val Class = CommunityFactory()
        fun getClass() = Class
    }

    fun create(data: CreationData, userDB: UserDBInterface, communityDB: CommunityDBInterface) {
        val user = userDB.select(data.login.email)
        val community = communityDB.select(data.name)

        lateinit var newCommunity: Community
        lateinit var userCommunities: List<String>

        if (user != null && community == null) {
            newCommunity = Community(name = data.name, imageURL = data.imageURL, id = UUID.randomUUID().toString(), about = data.about, creator = user.getEmail())
            userCommunities = user.getCommunities()
            userCommunities.add(newCommunity.getID())
            user.setCommunities(userCommunities)

            communityDB.insert(newCommunity)
            userDB.insert(user)
        }
    }

//    fun delete(data: MemberOperator, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface) {
//        val user = rwUser.select(data.login.email)
//        lateinit var notification: NotificationData
//        lateinit var mods: List<String>
//
//        if (user != null) {
//            val community = rwCommunity.select(data.identifier.ID)
//            if (community != null) {
//                when (community.getModerators().isEmpty()) {
//                    true -> rwCommunity.delete(community)
//                    false -> {
//                        notification = NotificationData(NotificationMainType.COMMUNITY, NotificationSubType.DELETE_REQUEST, community.getID())
//                        mods = community.getModerators()
//                        for (i in mods.indices) {
//                            val moderator = rwUser.select(mods[i])
//                            moderator?.updateInbox(notification)
//                        }
//                    }
//                }
//            }
//        }
//    }
}