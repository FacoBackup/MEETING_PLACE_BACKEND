package br.meetingplace.server.modules.topic.classes

import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime

class Topic(private var approved: Boolean, private val footer: String, private val creator: String,
            private var id: String, private val mainTopic: String?,private val creationDate: LocalDateTime): Table() {

    private var header: String? = null
    private var body: String? = null


    fun getCreationDate() = creationDate
    fun getApproved() = approved
    fun getID() = id
    fun getCreator() = creator
    fun getMainTopic() = mainTopic
    fun getHeader() = header
    fun getBody() = body
    fun getFooter() = footer

    fun setApproved(approved: Boolean) {
        this.approved = approved
    }
    fun setHeader(header: String) {
        this.header = header
    }
    fun setBody(body: String) {
        this.body = body
    }
}