package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.modules.global.dto.notification.NotificationData
import br.meetingplace.server.modules.global.dto.notification.types.NotificationMainType
import br.meetingplace.server.modules.global.dto.notification.types.NotificationSubType
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.requests.generic.operators.SimpleOperator

class Social private constructor() : SocialInterface {

    companion object {
        private val Class = Social()
        fun getClass() = Class
    }

    override fun follow(data: SimpleOperator, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface) {
        val user = rwUser.select(data.login.email)
        lateinit var notification: NotificationData


        if (user != null) {
            when (data.identifier.community) {
                false -> { //USER
                    val external = rwUser.select(data.identifier.ID)
                    notification = NotificationData(NotificationMainType.USER, NotificationSubType.FOLLOWING, user.getEmail())
                    if (external != null && user.getEmail() !in external.getFollowers()) {


                        external.updateInbox(notification)
                        external.updateFollowers(user.getEmail(), false)
                        user.updateFollowing(external.getEmail(), false)

                        rwUser.insert(user)
                        rwUser.insert(external)
                    }
                }
                true -> { //COMMUNITY
                    val community = rwCommunity.select(data.identifier.ID)

                    if (community != null && !community.verifyMember(data.login.email)) {
                        user.updateCommunitiesIFollow(community.getID(), false)
                        community.updateFollower(data.identifier.ID, false)
                        rwUser.insert(user)
                        rwCommunity.insert(community)
                    }
                }
            }

        }
    } //UPDATE

    override fun unfollow(data: SimpleOperator, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface) {
        val user = rwUser.select(data.login.email)

        if (user != null) {
            when (data.identifier.community) {
                false -> { //USER
                    val external = rwUser.select(data.identifier.ID)
                    if (external != null && user.getEmail() !in external.getFollowers()) {

                        external.updateFollowers(user.getEmail(), true)
                        user.updateFollowing(external.getEmail(), true)

                        rwUser.insert(user)
                        rwUser.insert(external)
                    }
                }
                true -> { //COMMUNITY
                    val community = rwCommunity.select(data.identifier.ID)

                    if (community != null && !community.verifyMember(data.login.email)) {
                        user.updateCommunitiesIFollow(community.getID(), true)
                        community.updateFollower(data.identifier.ID, true)
                        rwUser.insert(user)
                        rwCommunity.insert(community)
                    }
                }
            }
        }
    }
}