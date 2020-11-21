package br.meetingplace.server.modules.community.dto

import br.meetingplace.server.modules.members.dao.Members
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType


class Community(private val name: String, private val id: String, private var about: String?, creator: String, private var imageURL: String?): Members(){

    private var topicsInValidation = listOf<String>()
    private var approvedTopics = listOf<String>()
    private var reportIDs = listOf<String>()
    private var approvedGroups = listOf<String>()
    private var groupsInValidation = listOf<String>()

    fun getApprovedTopics() = approvedTopics
    fun getTopicsInValidation() = topicsInValidation
    fun getReports() = reportIDs
    fun getName() = name
    fun getID() = id
    fun getApprovedGroups() = approvedGroups
    fun getGroupsInValidation() = groupsInValidation

    init {
        updateMember(MemberData(creator, MemberType.MODERATOR), false)
    }

    fun setImage(imageURL: String?){
        this.imageURL = imageURL
    }

    fun setAbout(about: String?){
        this.about = about
    }

    fun setTopicsInValidation(topics: List<String>){
        topicsInValidation = topics
    }

    fun setReports(reports: List<String>){
        reportIDs = reports
    }

    fun setApprovedTopics(topics: List<String>){
        approvedTopics = topics
    }

    fun setGroupsInValidation(groups: List<String>){
        groupsInValidation = groups
    }

    fun setApprovedGroups(groups: List<String>){
        approvedGroups = groups
    }


}