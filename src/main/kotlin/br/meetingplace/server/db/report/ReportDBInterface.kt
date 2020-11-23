package br.meetingplace.server.db.report

import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.report.dto.Report

interface ReportDBInterface {
    fun select(id: String): Report?
    fun insert(data: Report): Status
    fun delete(data: Report): Status
    fun check(id: String): Boolean
}