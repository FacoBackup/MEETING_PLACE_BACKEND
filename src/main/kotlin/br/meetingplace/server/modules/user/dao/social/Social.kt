package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.global.dto.notification.NotificationData
import br.meetingplace.server.modules.global.dto.notification.types.NotificationMainType
import br.meetingplace.server.modules.global.dto.notification.types.NotificationSubType
import br.meetingplace.server.requests.generic.operators.SimpleOperator

class Social private constructor() {

    companion object {
        private val Class = Social()
        fun getClass() = Class
    }

    fun follow(data: SimpleOperator, userDB: UserDBInterface): Status {
        val user = userDB.select(data.login.email)
        lateinit var notification: NotificationData
        lateinit var externalFollowers: List<String>
        lateinit var userFollowing: List<String>
        lateinit var externalInbox: List<NotificationData>

        return if (user != null) {
            val external = userDB.select(data.identifier.ID)
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

                userDB.insert(user)
                userDB.insert(external)
                Status(statusCode = 200, StatusMessages.OK)
            }
            else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }

    fun unfollow(data: SimpleOperator, userDB: UserDBInterface):Status{
        val user = userDB.select(data.login.email)
        lateinit var externalFollowers: List<String>
        lateinit var userFollowing: List<String>

        return if (user != null) {
            val external = userDB.select(data.identifier.ID)
            if (external != null && user.getEmail() !in external.getFollowers()) {
                externalFollowers = external.getFollowers()
                externalFollowers.remove(user.getEmail())
                external.setFollowers(externalFollowers)

                userFollowing = user.getFollowing()
                userFollowing.remove(external.getEmail())
                user.setFollowing(userFollowing)

                userDB.insert(user)
                userDB.insert(external)
                Status(statusCode = 200, StatusMessages.OK)
            }
            else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        } else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
    }
}