package br.meetingplace.server.user.controller.social

import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface

import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.data.classes.notification.NotificationData
import br.meetingplace.server.data.classes.notification.data.NotificationMainType
import br.meetingplace.server.data.classes.notification.data.NotificationSubType

class Social private constructor() : SocialInterface {

    companion object {
        private val Class = Social()
        fun getClass() = Class
    }

    override fun follow(data: SimpleOperator, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface) {
        val user = rwUser.load(data.login.email)
        lateinit var notification: NotificationData


        if (user != null) {
            when (data.identifier.community) {
                false -> { //USER
                    val external = rwUser.load(data.identifier.ID)
                    notification = NotificationData(NotificationMainType.USER, NotificationSubType.FOLLOWING)
                    if (external != null && user.getEmail() !in external.getFollowers()) {


                        external.updateInbox(notification)
                        external.updateFollowers(user.getEmail(), false)
                        user.updateFollowing(external.getEmail(), false)

                        rwUser.store(user)
                        rwUser.store(external)
                    }
                }
                true -> { //COMMUNITY
                    val community = rwCommunity.load(data.identifier.ID)

                    if (community != null && !community.verifyMember(data.login.email)) {
                        user.updateCommunitiesIFollow(community.getID(), false)
                        community.updateFollower(data.identifier.ID, false)
                        rwUser.store(user)
                        rwCommunity.store(community)
                    }
                }
            }

        }
    } //UPDATE

    override fun unfollow(data: SimpleOperator, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface) {
        val user = rwUser.load(data.login.email)

        if (user != null) {
            when (data.identifier.community) {
                false -> { //USER
                    val external = rwUser.load(data.identifier.ID)
                    if (external != null && user.getEmail() !in external.getFollowers()) {

                        external.updateFollowers(user.getEmail(), true)
                        user.updateFollowing(external.getEmail(), true)

                        rwUser.store(user)
                        rwUser.store(external)
                    }
                }
                true -> { //COMMUNITY
                    val community = rwCommunity.load(data.identifier.ID)

                    if (community != null && !community.verifyMember(data.login.email)) {
                        user.updateCommunitiesIFollow(community.getID(), true)
                        community.updateFollower(data.identifier.ID, true)
                        rwUser.store(user)
                        rwCommunity.store(community)
                    }
                }
            }
        }
    }
}