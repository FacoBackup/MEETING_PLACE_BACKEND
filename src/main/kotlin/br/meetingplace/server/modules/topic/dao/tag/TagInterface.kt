package br.meetingplace.server.modules.topic.dao.tag

import br.meetingplace.server.modules.topic.dto.response.TagDTO
import br.meetingplace.server.modules.topic.dto.response.TopicTagDTO

interface TagInterface {
    suspend fun create(tag: String): Long?
    suspend fun read(tag: String): TagDTO?
    suspend fun readRank(): List<TagDTO>
    suspend fun readByMaxID(tagID: Long, maxID: Long): List<TopicTagDTO>
    suspend fun readNewest(tagID: Long):List<TopicTagDTO>
}