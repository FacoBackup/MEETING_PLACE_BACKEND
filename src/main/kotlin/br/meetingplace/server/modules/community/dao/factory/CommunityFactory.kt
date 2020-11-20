package br.meetingplace.server.modules.community.dao.factory

import br.meetingplace.server.modules.notification.dto.NotificationData
import br.meetingplace.server.modules.notification.dto.types.NotificationMainType
import br.meetingplace.server.modules.notification.dto.types.NotificationSubType
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.community.dto.Community
import br.meetingplace.server.requests.generic.data.CreationData
import br.meetingplace.server.requests.generic.operators.MemberOperator

class CommunityFactory private constructor() {

    companion object {
        private val Class = CommunityFactory()
        fun getClass() = Class
    }

    private fun getCommunityID(name: String): String{
        return (name.replace("\\s".toRegex(), "")).toLowerCase()
    }

    fun create(data: CreationData, userDB: UserDBInterface, communityDB: CommunityDBInterface) {
        val user = userDB.select(data.login.email)
        val community = communityDB.select(data.name)

        lateinit var newCommunity: Community
        lateinit var id: String

        if (user != null && community == null) {
            newCommunity = Community(name = data.name, imageURL = data.imageURL,id =  getCommunityID(data.name), about = data.about, creator = user.getEmail())
            id = getCommunityID(data.name)
            user.updateModeratorIn(id, false)
            communityDB.insert(newCommunity)
            userDB.insert(user)
        }
    }

    fun delete(data: MemberOperator, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface) {
        val user = rwUser.select(data.login.email)
        lateinit var notification: NotificationData
        lateinit var mods: List<String>

        if (user != null) {
            val community = rwCommunity.select(data.identifier.ID)
            if (community != null) {
                when (community.getModerators().isEmpty()) {
                    true -> rwCommunity.delete(community)
                    false -> {
                        notification = NotificationData(NotificationMainType.COMMUNITY, NotificationSubType.DELETE_REQUEST, community.getID())
                        mods = community.getModerators()
                        for (i in mods.indices) {
                            val moderator = rwUser.select(mods[i])
                            moderator?.updateInbox(notification)
                        }
                    }
                }
            }
        }
    }
}