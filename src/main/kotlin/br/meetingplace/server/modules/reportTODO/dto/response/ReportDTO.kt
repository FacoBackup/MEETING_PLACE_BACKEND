package br.meetingplace.server.modules.reportTODO.dto.response

data class ReportDTO (val reportID: Long,
                      val creatorID: Long,
                      val topicID: Long,
                      val reason: String?,
                      val communityID: Long,
                      val creationDate: String,
                      val done: Boolean,
                      val responseCreatorID: Long?,
                      val response: String?)