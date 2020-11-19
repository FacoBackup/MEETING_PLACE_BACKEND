package br.meetingplace.server.services.community.controller.factory

import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.CreationData
import br.meetingplace.server.dto.MemberOperator
import br.meetingplace.server.services.community.classes.Community
import br.meetingplace.server.data.classes.notification.NotificationData
import br.meetingplace.server.data.classes.notification.data.NotificationMainType
import br.meetingplace.server.data.classes.notification.data.NotificationSubType

class CommunityFactory private constructor() {

    companion object {
        private val Class = CommunityFactory()
        fun getClass() = Class
    }

    private fun getCommunityID(name: String): String{
        return (name.replace("\\s".toRegex(), "")).toLowerCase()
    }

    fun create(data: CreationData, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface) {
        val user = rwUser.load(data.login.email)
        val community = rwCommunity.load(data.name)

        lateinit var newCommunity: Community
        lateinit var id: String

        if (user != null && community == null) {
            newCommunity = Community(data.name, getCommunityID(data.name), data.about, user.getEmail())
            id = getCommunityID(data.name)
            user.updateModeratorIn(id, false)
            rwCommunity.store(newCommunity)
            rwUser.store(user)
        }
    }

    fun delete(data: MemberOperator, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface) {
        val user = rwUser.load(data.login.email)
        lateinit var notification: NotificationData
        lateinit var mods: List<String>

        if (user != null) {
            val community = rwCommunity.load(data.identifier.ID)
            if (community != null) {
                when (community.getModerators().isEmpty()) {
                    true -> rwCommunity.delete(community)
                    false -> {
                        notification = NotificationData(NotificationMainType.COMMUNITY, NotificationSubType.DELETE_REQUEST)
                        mods = community.getModerators()
                        for (i in mods.indices) {
                            val moderator = rwUser.load(mods[i])
                            moderator?.updateInbox(notification)
                        }
                    }
                }
            }
        }
    }
}