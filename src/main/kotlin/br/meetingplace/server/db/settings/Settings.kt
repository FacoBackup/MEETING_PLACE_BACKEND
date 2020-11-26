package br.meetingplace.server.db.settings

import org.jetbrains.exposed.sql.Database
import org.postgresql.util.PSQLException
import java.sql.SQLException

fun dbSettings(host: String,dbName: String,user: String, password: String): Database?{
    return try{
        Database.connect(url = "jdbc:postgresql://$host/$dbName", driver = "org.postgresql.Driver", user = user, password = password)
    }catch(sql: SQLException ){
        null
    }catch (psql: PSQLException){
        null
    }
}