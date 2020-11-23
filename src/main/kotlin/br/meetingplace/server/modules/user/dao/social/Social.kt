package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.dto.notification.NotificationData
import br.meetingplace.server.modules.global.dto.notification.types.NotificationMainType
import br.meetingplace.server.modules.global.dto.notification.types.NotificationSubType
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.requests.generic.operators.SimpleOperator
import kotlin.collections.indices as indices

class Social private constructor() {

    companion object {
        private val Class = Social()
        fun getClass() = Class
    }

    fun follow(data: SimpleOperator, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface) {
        val user = rwUser.select(data.login.email)
        lateinit var notification: NotificationData
        lateinit var externalFollowers: List<String>
        lateinit var userFollowing: List<String>
        lateinit var userCommunities: List<MemberData>
        lateinit var externalInbox: List<NotificationData>

        if (user != null) {
            when (data.identifier.community) {
                false -> { //USER
                    val external = rwUser.select(data.identifier.ID)
                    notification = NotificationData(NotificationMainType.USER, NotificationSubType.FOLLOWING, user.getEmail())
                    if (external != null && user.getEmail() !in external.getFollowers()) {
                        externalFollowers = external.getFollowers()
                        externalFollowers.add(user.getEmail())
                        external.setFollowers(externalFollowers)

                        externalInbox = external.getInbox()
                        externalInbox.add(notification)
                        external.setInbox(externalInbox)

                        userFollowing = user.getFollowing()
                        userFollowing.add(external.getEmail())
                        user.setFollowing(userFollowing)

                        rwUser.insert(user)
                        rwUser.insert(external)
                    }
                }
                true -> { //COMMUNITY
                    val community = rwCommunity.select(data.identifier.ID)
                    if (community != null && !community.verifyMember(data.login.email)) {

                        userCommunities = user.getCommunities()
                        userCommunities.add(MemberData(community.getID(), MemberType.NORMAL))
                        user.setCommunities(userCommunities)

                        community.updateMember(data.identifier.ID, MemberType.NORMAL, false)
                        rwUser.insert(user)
                        rwCommunity.insert(community)
                    }
                }
            }
        }
    }

    fun unfollow(data: SimpleOperator, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface) {
        val user = rwUser.select(data.login.email)
        lateinit var externalFollowers: List<String>
        lateinit var userFollowing: List<String>
        lateinit var userCommunities: List<MemberData>
        if (user != null) {
            when (data.identifier.community) {
                false -> { //USER
                    val external = rwUser.select(data.identifier.ID)
                    if (external != null && user.getEmail() !in external.getFollowers()) {
                        externalFollowers = external.getFollowers()
                        externalFollowers.remove(user.getEmail())
                        external.setFollowers(externalFollowers)

                        userFollowing = user.getFollowing()
                        userFollowing.remove(external.getEmail())
                        user.setFollowing(userFollowing)

                        rwUser.insert(user)
                        rwUser.insert(external)
                    }
                }
                true -> { //COMMUNITY
                    val community = rwCommunity.select(data.identifier.ID)

                    if (community != null && !community.verifyMember(data.login.email)) {
                        val role = community.getMemberRole(user.getEmail())
                        if (role != null) {

                            userCommunities = user.getCommunities()
                            userCommunities.remove(MemberData(community.getID(), role))
                            user.setCommunities(userCommunities)

                            community.updateMember(data.identifier.ID, role, true)
                            rwUser.insert(user)
                            rwCommunity.insert(community)
                        }
                    }
                }
            }
        }
    }
}