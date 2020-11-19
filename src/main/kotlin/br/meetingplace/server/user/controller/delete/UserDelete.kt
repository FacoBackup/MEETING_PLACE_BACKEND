package br.meetingplace.server.user.controller.delete

import br.meetingplace.server.loadstore.interfaces.TopicLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.Login

class UserDelete private constructor() : UserDeleteInterface {
    companion object {
        private val Class = UserDelete()
        fun getClass() = Class
    }

    override fun delete(data: Login, rwUser: UserLSInterface, rwTopic: TopicLSInterface) {
        val user = rwUser.load(data.email)

        lateinit var followers: List<String>
        lateinit var following: List<String>

        if (user != null && data.email == user.getEmail() && data.password == user.getPassword()) {
            followers = user.getFollowers()
            following = user.getFollowing()
            println(followers)
            println(following)
            for (index in followers.indices) {
                val userExternal = rwUser.load(followers[index])
                if (userExternal != null) {
                    userExternal.updateFollowing(user.getEmail(), true)
                    rwUser.store(userExternal)
                }
            }

            for (index in following.indices) {
                val userExternal = rwUser.load(following[index])
                if (userExternal != null) {
                    userExternal.updateFollowers(user.getEmail(), true)
                    rwUser.store(userExternal)
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

    private fun deleteAllTopicsFromUser(data: Login, rwUser: UserLSInterface, rwTopic: TopicLSInterface) {
        val user = rwUser.load(data.email)

        if (user != null) {
            val myTopics = user.getMyTopics()
            for (element in myTopics) {
                val topic = rwTopic.load(element, null)
                if (topic != null)
                    rwTopic.delete(topic)
            }
        }
    } //DELETE
}