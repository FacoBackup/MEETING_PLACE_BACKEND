package br.meetingplace.server.modules.topic.dto

class Topic(private var approved: Boolean, private val footer: String, private val creator: String, private var id: String, private val mainTopic: String?) {

    private var comments = mutableListOf<String>()
    private var header: String? = null
    private var body: String? = null
    private var likes = mutableListOf<String>()
    private var dislikes = mutableListOf<String>()

    fun getApproved() = approved
    fun getID() = id
    fun getCreator() = creator
    fun getMainTopic() = mainTopic
    fun getComments() = comments
    fun getHeader() = header
    fun getBody() = body
    fun getFooter() = footer
    fun getLikes() = likes
    fun getDislikes() = dislikes

    fun setApproved(approved: Boolean) {
        this.approved = approved
    }

    fun setLikes(likes: List<String>) {
        this.likes = likes as MutableList<String>
    }

    fun setDislikes(dislikes: List<String>) {
        this.dislikes = dislikes as MutableList<String>
    }

    fun setHeader(header: String) {
        this.header = header
    }

    fun setBody(body: String) {
        this.body = body
    }

    fun setComments(comments: List<String>) {
        this.comments = comments as MutableList<String>
    }
}