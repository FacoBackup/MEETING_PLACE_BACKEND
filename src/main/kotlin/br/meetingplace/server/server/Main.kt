package br.meetingplace.server.server

import br.meetingplace.server.settings.db.DBSettings

fun main() {
    DBSettings.dbSettings(host = "localhost", dbName = "api_db", user = "api", password = "12345")
    DBSettings.setUpTables()

    startServer()
}