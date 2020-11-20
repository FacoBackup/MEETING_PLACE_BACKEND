package br.meetingplace.server.modules.user.dto.dependencies.services.follow

interface UserFollowInterface {
    fun updateFollowers(data: String, remove: Boolean)
    fun updateFollowing(data: String, remove: Boolean)
    fun getFollowing(): List<String>
    fun getFollowers(): List<String>
}