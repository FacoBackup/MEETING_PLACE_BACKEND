package br.meetingplace.server.modules.topic.dto.dependencies.content

interface TopicContentInterface {
    fun addContent(header: String, body: String, footer: String)
    fun editHeader(header: String)
    fun editBody(body: String)
}