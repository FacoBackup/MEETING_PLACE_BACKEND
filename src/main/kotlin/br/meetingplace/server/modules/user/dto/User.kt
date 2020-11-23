package br.meetingplace.server.modules.user.dto

import br.meetingplace.server.modules.chat.dto.ChatIdentifier
import br.meetingplace.server.modules.global.dto.notification.NotificationData
import br.meetingplace.server.modules.members.dto.MemberData

class User(
        private var userName: String,
        private var age: Int,
        private var email: String,
        private var password: String
){

    private var gender: String? = null
    private var nationality: String? = null
    private var about: String? = null
    private var imageURL: String? = null
    private var chats = mutableListOf<ChatIdentifier>()
    private var communities = mutableListOf<MemberData>()
    private var followers = mutableListOf<String>()
    private var following = mutableListOf<String>()
    private var groups = mutableListOf<MemberData>()
    private var inbox = mutableListOf<NotificationData>()
    private var topics = mutableListOf<String>()

    //GETTERS
    fun getTopics() = topics
    fun getInbox() = inbox
    fun getGroups() = groups
    fun getFollowing() = following
    fun getFollowers() = followers
    fun getCommunities() = communities
    fun getChats() = chats
    fun getImageURL() = imageURL
    fun getPassword() = password
    fun getAge() = age
    fun getEmail() = email
    fun getUserName() = userName
    fun getNationality() = nationality
    fun getAbout() = about
    fun getGender() = gender

    //SETTERS
    fun setImageURL(imageURL: String?){
        this.imageURL = imageURL
    }
    fun setTopics(topics: List<String>) {
        this.topics = topics as MutableList<String>
    }
    fun setInbox(inbox: List<NotificationData>){
        this.inbox = inbox as MutableList<NotificationData>
    }
    fun setGroups(groups: List<MemberData>){
        this.groups = groups as MutableList<MemberData>
    }
    fun setFollowers(followers: List<String>) {
        this.followers = followers as MutableList<String>
    }
    fun setFollowing(following: List<String>) {
        this.followers = following as MutableList<String>
    }
    fun setCommunities(communities: List<MemberData>) {
        this.communities = communities as MutableList<MemberData>
    }
    fun setChats(chats: List<ChatIdentifier>) {
        this.chats = chats as MutableList<ChatIdentifier>
    }
    fun setAbout(about: String?) {
        this.about = about
    }
    fun setNationality(nationality: String?){
        this.nationality = nationality
    }
    fun setGender(gender: String?){
        this.gender = gender
    }
}