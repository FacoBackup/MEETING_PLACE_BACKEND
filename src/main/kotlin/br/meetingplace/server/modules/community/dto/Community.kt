package br.meetingplace.server.modules.community.dto

import br.meetingplace.server.modules.members.dao.Members
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType


class Community(private val name: String, private val id: String, private var about: String?, creator: String, private var imageURL: String?): Members(){

    private var topicsInValidation = mutableListOf<String>()
    private var approvedTopics = mutableListOf<String>()
    private var reportIDs = mutableListOf<String>()
    private var approvedGroups = mutableListOf<String>()
    private var groupsInValidation = mutableListOf<String>()

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
        topicsInValidation = topics as MutableList<String>
    }

    fun setReports(reports: List<String>){
        reportIDs = reports as MutableList<String>
    }

    fun setApprovedTopics(topics: List<String>){
        approvedTopics = topics as MutableList<String>
    }

    fun setGroupsInValidation(groups: List<String>){
        groupsInValidation = groups as MutableList<String>
    }

    fun setApprovedGroups(groups: List<String>){
        approvedGroups = groups as MutableList<String>
    }


}