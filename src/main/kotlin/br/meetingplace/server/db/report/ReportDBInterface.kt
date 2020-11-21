package br.meetingplace.server.db.report

import br.meetingplace.server.modules.report.dto.Report

interface ReportDBInterface {
    fun select(id: String): Report?
    fun insert(data: Report)
    fun delete(data: Report)
}