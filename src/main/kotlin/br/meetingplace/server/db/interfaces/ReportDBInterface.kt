package br.meetingplace.server.db.interfaces

import br.meetingplace.server.modules.community.dto.dependencies.data.Report

interface ReportDBInterface {
    fun select(id: String): Report?
    fun insert(data: Report)
    fun delete(data: Report)
}