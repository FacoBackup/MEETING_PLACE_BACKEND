package br.meetingplace.server.methods

interface AESInterface {
    suspend fun encrypt(myKey: String, data: String): String?
    suspend fun decrypt(myKey: String, data: String): String?
}