package br.meetingplace.server.modules.report.dto

data class ReportDTO (val reportID: String, val creatorID: String,
                      val topicID: String, val reason: String?, val communityID: String,
                      val creationDate: String, var status: Short,
                      var response: String?)