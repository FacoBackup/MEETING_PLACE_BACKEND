package br.meetingplace.server.services.topic.classes.dependencies.content

interface TopicContentInterface {
    fun addContent(header: String, body: String, footer: String)
    fun editHeader(header: String)
    fun editBody(body: String)
}