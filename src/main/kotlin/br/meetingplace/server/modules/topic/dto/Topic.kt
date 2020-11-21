package br.meetingplace.server.modules.topic.dto

class Topic(private val footer: String, private val creator: String, private var id: String, private val mainTopic: String?, private val type: TopicType) {

    private var comments = listOf<String>()
    private var header: String? = null
    private var body: String? = null
    private var likes = listOf<String>()
    private var dislikes = listOf<String>()

    fun getID() = id
    fun getCreator() = creator
    fun getMainTopic() = mainTopic
    fun getComments() = comments
    fun getHeader ()= header
    fun getBody () = body
    fun getFooter () = footer
    fun getLikes() = likes
    fun getDislikes() = dislikes

    fun setLikes(likes: List<String>){
        this.likes = likes
    }
    fun setDislikes(dislikes: List<String>){
        this.dislikes = dislikes
    }
    fun setHeader (header: String){
        this.header = header
    }
    fun setBody (body: String){
        this.body = body
    }
    fun setComments(comments: List<String>){
        this.comments = comments
    }
}