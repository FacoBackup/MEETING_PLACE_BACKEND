package br.meetingplace.server.modules.reportTODO.dto.response

data class ReportDTO (val reportID: String, val creatorID: String,
                      val topicID: String, val reason: String?, val communityID: String,
                      val creationDate: String, var done: Boolean,
                      var response: String?)