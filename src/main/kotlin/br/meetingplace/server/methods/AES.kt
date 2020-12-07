package br.meetingplace.server.methods

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AES: AESInterface{
    private var key: ByteArray? = null
    private var secret: SecretKeySpec? = null

    override fun encrypt(myKey: String, data: String): String?{
        return try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            setKey(myKey)
            cipher.init(Cipher.ENCRYPT_MODE, secret)
            Base64.getEncoder().encodeToString(cipher.doFinal(data.toByteArray()))
        }catch (e: Exception){
            null
        }
    }

    override fun decrypt(myKey: String, data: String): String?{
        return try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            setKey(myKey)
            cipher.init(Cipher.DECRYPT_MODE, secret)
            String(cipher.doFinal(Base64.getDecoder().decode(data)))
        }catch (e: Exception){
            null
        }
    }
    private fun setKey(myKey: String){
        lateinit var sha: MessageDigest
        try {
            key = myKey.toByteArray()
            sha = MessageDigest.getInstance("SHA-1")
            key = sha.digest(key)

            if(key != null)
                key = (key!!).copyOf(16)

            secret = SecretKeySpec(key, "AES")
        }catch (e: NoSuchAlgorithmException){
            println(e.message)
        }catch (e: UnsupportedEncodingException){
            println(e.message)
        }
    }
}
