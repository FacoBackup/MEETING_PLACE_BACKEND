package br.meetingplace.server.modules.user.dao.delete

import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.requests.generic.data.Login

class UserDelete private constructor() : UserDeleteInterface {
    companion object {
        private val Class = UserDelete()
        fun getClass() = Class
    }

    override fun delete(data: Login, rwUser: UserDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.email)

        lateinit var followers: List<String>
        lateinit var following: List<String>

        if (user != null && data.email == user.getEmail() && data.password == user.getPassword()) {
            followers = user.getFollowers()
            following = user.getFollowing()
            println(followers)
            println(following)
            for (index in followers.indices) {
                val userExternal = rwUser.select(followers[index])
                if (userExternal != null) {
                    userExternal.updateFollowing(user.getEmail(), true)
                    rwUser.insert(userExternal)
                }
            }

            for (index in following.indices) {
                val userExternal = rwUser.select(following[index])
                if (userExternal != null) {
                    userExternal.updateFollowers(user.getEmail(), true)
                    rwUser.insert(userExternal)
                }
            }
            /*
                for(i in 0 until groupList.size){
                    member = UserMember(management,groupList[i].getEmail())
                    removeMember(member) // should use an override here
                }
             */

            rwUser.delete(user)
            deleteAllTopicsFromUser(data, rwUser = rwUser, rwTopic= rwTopic)
        }
    }

    private fun deleteAllTopicsFromUser(data: Login, rwUser: UserDBInterface, rwTopic: TopicDBInterface) {
        val user = rwUser.select(data.email)

        if (user != null) {
            val myTopics = user.getMyTopics()
            for (element in myTopics) {
                val topic = rwTopic.select(element, null)
                if (topic != null)
                    rwTopic.delete(topic)
            }
        }
    } //DELETE
}