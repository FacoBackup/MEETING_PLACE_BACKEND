package br.meetingplace.server.modules.report.dto

data class Report(
        val reportID: String,
        val creator: String,
        val serviceID: String,
        val reason: String?,
        var finished: Boolean,
        val communityId: String,
        val response: String?
)
