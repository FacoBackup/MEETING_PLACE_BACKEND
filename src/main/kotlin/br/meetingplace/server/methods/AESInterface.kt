package br.meetingplace.server.methods

interface AESInterface {
    fun encrypt(myKey: String, data: String): String?
    fun decrypt(myKey: String, data: String): String?
}