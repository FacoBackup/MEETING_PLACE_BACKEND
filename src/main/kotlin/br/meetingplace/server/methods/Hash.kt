package br.meetingplace.server.methods

import java.math.BigInteger
import java.security.MessageDigest


fun hashString(encryption: String, data: String): String{

    val md = MessageDigest.getInstance(encryption)
    return BigInteger(1, md.digest(data.toByteArray())).toString(16).padStart(32, '0')
}