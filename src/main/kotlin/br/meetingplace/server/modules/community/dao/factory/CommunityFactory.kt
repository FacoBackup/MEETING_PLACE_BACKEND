package br.meetingplace.server.modules.community.dao.factory

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.community.classes.Community
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.requests.generic.data.CreationData
import java.util.*

object CommunityFactory {

    fun create(data: CreationData, userDB: UserDBInterface, communityDB: CommunityDBInterface): Status {
        val user = userDB.select(data.login.email)
        val community = communityDB.select(data.name)

        lateinit var newCommunity: Community
        lateinit var userCommunities: List<String>

        return if (user != null && community == null) {
            newCommunity = Community(name = data.name, imageURL = data.imageURL, id = UUID.randomUUID().toString(), about = data.about, creator = user.getEmail())
            userCommunities = user.getCommunities()
            userCommunities.add(newCommunity.getID())
            user.setCommunities(userCommunities)

            userDB.insert(user)
            return communityDB.insert(newCommunity)
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
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