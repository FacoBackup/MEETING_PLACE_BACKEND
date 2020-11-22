package br.meetingplace.server.modules.community.dto

import br.meetingplace.server.modules.members.dao.Members
import br.meetingplace.server.modules.members.dto.MemberType


class Community(private val name: String, private val id: String, private var about: String?, creator: String, private var imageURL: String?) : Members() {

    init {
        updateMember(creator, MemberType.MODERATOR, false)
    }

    private var topics = mutableListOf<String>()
    private var reportIDs = mutableListOf<String>()
    private var groups = mutableListOf<String>()

    fun getTopics() = topics
    fun getReports() = reportIDs
    fun getName() = name
    fun getID() = id
    fun getGroups() = groups

    fun setImage(imageURL: String?) {
        this.imageURL = imageURL
    }

    fun setAbout(about: String?) {
        this.about = about
    }

    fun setReports(reports: List<String>) {
        reportIDs = reports as MutableList<String>
    }

    fun setTopics(topics: List<String>) {
        this.topics = topics as MutableList<String>
    }

    fun setGroups(groups: List<String>) {
        this.groups = groups as MutableList<String>
    }
}