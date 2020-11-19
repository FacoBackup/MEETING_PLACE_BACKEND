package br.meetingplace.server.services.topic.classes.dependencies.opinions

interface TopicOpinionInterface {
    fun like(email: String)
    fun removeLike(email: String)
    fun dislike(email: String)
    fun removeDislike(email: String)
    fun likeToDislike(email: String)
    fun dislikeToLike(email: String)
    fun getLikes(): List<String>
    fun getDislikes(): List<String>

}