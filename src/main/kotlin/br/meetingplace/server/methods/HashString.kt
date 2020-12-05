package br.meetingplace.server.methods

import java.math.BigInteger
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter


fun hashString(encryption: String, data: String): String{

    val md = MessageDigest.getInstance(encryption)
    println(BigInteger(1, md.digest(data.toByteArray())).toString(16).padStart(32, '0'))
    return BigInteger(1, md.digest(data.toByteArray())).toString(16).padStart(32, '0')
}