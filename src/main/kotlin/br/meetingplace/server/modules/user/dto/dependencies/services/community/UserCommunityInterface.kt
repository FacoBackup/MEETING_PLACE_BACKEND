package br.meetingplace.server.modules.user.dto.dependencies.services.community

interface UserCommunityInterface {
    fun updateModeratorIn(id: String, leave: Boolean)
    fun updateCommunitiesIFollow(id: String, unfollow: Boolean)
    fun getModeratorIn(): List<String>
    fun getCommunitiesIFollow(): List<String>
}