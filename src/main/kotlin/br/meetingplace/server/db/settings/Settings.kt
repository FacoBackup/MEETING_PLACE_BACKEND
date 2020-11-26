package br.meetingplace.server.db.settings

import org.jetbrains.exposed.sql.Database

fun dbSettings(host: String,dbName: String,driver: String ,user: String, password: String): Database?{
    return try{
        Database.connect(url = "jdbc:postgresql://$host/$dbName", driver = driver, user = user, password = password)
    }catch(e: Exception){
        null
    }
}