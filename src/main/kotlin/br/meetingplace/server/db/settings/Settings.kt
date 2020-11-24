package br.meetingplace.server.db.settings

import org.jetbrains.exposed.sql.Database

object Settings {
    val db = Database.connect("jdbc:postgresql://192.168.1.222/testapp", driver = "org.postgresql.Driver", user = "user_testeapp", password = "kU01ZUQXH0")
}