package br.meetingplace.server.modules.user.dao.delete

import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.global.methods.member.getMemberRole
import br.meetingplace.server.modules.members.classes.MemberData
import br.meetingplace.server.requests.generic.data.Login

object UserDelete {

    fun delete(data: Login, userDB: UserDBInterface, topicDB: TopicDBInterface, groupDB: GroupDBInterface, communityDB: CommunityDBInterface) : Status {
        val user = userDB.select(data.email)

        lateinit var userFollowers: List<String>
        lateinit var userFollowing: List<String>
        lateinit var userGroups: List<String>
        lateinit var userCommunities: List<String>
        lateinit var externalFollowing: List<String>
        lateinit var externalFollowers: List<String>
        lateinit var members: List<MemberData>

        return if (user != null && data.password == user.getPassword()) {
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
                    members = group.getMembers()
                    val role = getMemberRole(group.getMembers(),user.getEmail())
                    if(role != null)
                        members.remove(MemberData(user.getEmail(), role))
                    group.setMembers(members)

                    groupDB.insert(group)
                }
            }

            for (index in userCommunities.indices) {
                val community = communityDB.select(userCommunities[index])
                if (community != null) {
                    members = community.getMembers()
                    val role = getMemberRole(community.getMembers(),user.getEmail())
                    if(role != null)
                        members.remove(MemberData(user.getEmail(), role))
                    community.setMembers(members)

                    communityDB.insert(community)
                }
            }


            deleteAllTopicsFromUser(data, rwUser = userDB, rwTopic = topicDB)
            return userDB.delete(user)
        }
        else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
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