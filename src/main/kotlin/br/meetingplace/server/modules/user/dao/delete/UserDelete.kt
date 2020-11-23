package br.meetingplace.server.modules.user.dao.delete

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.requests.generic.data.Login

class UserDelete private constructor() {
    companion object {
        private val Class = UserDelete()
        fun getClass() = Class
    }

    fun delete(data: Login, userDB: UserDBInterface, topicDB: TopicDBInterface, groupDB: GroupDBInterface, communityDB: CommunityDBInterface) {
        val user = userDB.select(data.email)

        lateinit var userFollowers: List<String>
        lateinit var userFollowing: List<String>
        lateinit var userGroups: List<String>
        lateinit var userCommunities: List<String>
        lateinit var externalFollowing: List<String>
        lateinit var externalFollowers: List<String>

        if (user != null && data.email == user.getEmail() && data.password == user.getPassword()) {
            userFollowers = user.getFollowers()
            userFollowing = user.getFollowing()
            userGroups = user.getGroups()
            userCommunities = user.getCommunities()

            for (index in userFollowers.indices) {
                val external = userDB.select(userFollowers[index])
                if (external != null) {
                    externalFollowing = external.getFollowing()
                    externalFollowing.remove(user.getEmail())
                    external.setFollowing(externalFollowing)
                    userDB.insert(external)
                }
            }

            for (index in userFollowing.indices) {
                val external = userDB.select(userFollowing[index])
                if (external != null) {
                    externalFollowers = external.getFollowers()
                    externalFollowers.remove(user.getEmail())
                    external.setFollowing(externalFollowers)

                    userDB.insert(external)
                }
            }

            for (index in userGroups.indices) {
                val group = groupDB.select(userGroups[index])
                if (group != null) {
                    group.removeMember(user.getEmail())
                    groupDB.insert(group)
                }
            }

            for (index in userCommunities.indices) {
                val community = communityDB.select(userCommunities[index])
                if (community != null) {
                    community.removeMember(user.getEmail())
                    communityDB.insert(community)
                }
            }

            userDB.delete(user)
            deleteAllTopicsFromUser(data, rwUser = userDB, rwTopic = topicDB)
        }
    }

    private fun deleteAllTopicsFromUser(data: Login, rwUser: UserDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.email)
        lateinit var topics: List<String>
        if (user != null) {
            topics = user.getTopics()
            for (element in topics) {
                val topic = rwTopic.select(element, null)
                if (topic != null)
                    rwTopic.delete(topic)
            }
        }
    } //DELETE
}