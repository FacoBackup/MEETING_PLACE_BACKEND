package br.meetingplace.server.loadstore.interfaces

import br.meetingplace.server.services.community.classes.dependencies.data.Report

interface ReportLSInterface {
    fun delete(data: Report)
    fun load(id: String): Report?
    fun store(data: Report)
}