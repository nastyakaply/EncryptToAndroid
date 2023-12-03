package com.example.myapplication

import android.R
import android.util.Base64
import android.util.Log
import android.widget.TextView
import java.security.Key
import java.security.KeyPairGenerator
import java.util.Date
import javax.crypto.Cipher


object RSA {
    private var publicKey: Key? = null
    private var privateKey: Key? = null
    private var timeStart: Date? = null
    private var timeStop: Date? = null
    var timeRun: Long
        private set(value) {}
        get() {
            return (timeStop?.time ?: 0L) - (timeStart?.time ?: 0L)
        }

    private fun genKeys() {
        // Generate key pair for 1024-bit RSA encryption and decryption
        try {
            val kpg = KeyPairGenerator.getInstance("RSA")
            kpg.initialize(2048)
            val kp = kpg.genKeyPair()
            publicKey = kp.public
            privateKey = kp.private
        } catch (e: Exception) {
            Log.e("Crypto", "RSA key pair error")
        }
    }

    suspend fun encrypt(message: String): String? {
        genKeys()
        timeStart = Date()
        // Encode the original data with RSA private key
        var encodedBytes: ByteArray? = null
        try {
            val c = Cipher.getInstance("RSA")
            c.init(Cipher.ENCRYPT_MODE, privateKey)
            val messageToByte = message.toByteArray()
            encodedBytes = c.doFinal(messageToByte)
        } catch (e: Exception) {
            Log.e("Crypto", "RSA encryption error")
        }
        timeStop = Date()
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT)
    }

    suspend fun decrypt(encryptMessage: String): String? {
        timeStart = Date()
        val messageBytes = Base64.decode(encryptMessage, Base64.DEFAULT)
        // Decode the encoded data with RSA public key
        var decodedBytes: ByteArray? = null
        try {
            val c = Cipher.getInstance("RSA")
            c.init(Cipher.DECRYPT_MODE, publicKey)
            decodedBytes = c.doFinal(messageBytes)
        } catch (e: Exception) {
            Log.e("Crypto", "RSA decryption error")
        }
        timeStop = Date()
        return decodedBytes?.decodeToString()
    }

//    const val COMMON_KEY = 159
//    const val OPEN_KEY = 43
//    const val CLOSE_KEY = 75
//    val ALFAVIT = listOf<Char>('а','б','в','г','д','е','ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х','ц','ч','ш','щ','ъ','ы','ь','э','ю','я',' ')
//
//    fun encrypt(message: String): List<Int> {
//        Log.d("test", "Шифрование началось")
//        return convertToNumbForm(message).map {
//            (it.toFloat().pow(OPEN_KEY) % COMMON_KEY).toInt()
//        }
//    }
//
//    fun decrypt(encryptMessage: List<Int>): String {
//        Log.d("test", "Расшифровывание началось")
//        encryptMessage.forEach {
//            var a = (it.toFloat().pow(CLOSE_KEY) % COMMON_KEY).toInt()
//            var b = ALFAVIT.getOrNull(a)
//        }
//
//        return encryptMessage.map {
//            ALFAVIT.getOrNull((it.toFloat().pow(CLOSE_KEY) % COMMON_KEY).toInt())
//        }.toString()
//    }
//
//    private fun convertToNumbForm(message: String): List<Int> {
//        return message.toCharArray().map {
//            ALFAVIT.indexOf(it)
//        }
//    }



//    fun generateRSAKeyPair(alias: String) {
//        val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")
//        val spec = RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4)
//        keyPairGenerator.initialize(spec)
//        val keyPair = keyPairGenerator.generateKeyPair()
//
//        val privateKey = keyPair.private as PrivateKey
//        val publicKey = keyPair.public as PublicKey
//
//        val keyStore = KeyStore.getInstance("AndroidKeyStore")
//        keyStore.load(null)
//        keyStore.setKeyEntry(alias, privateKey, null, arrayOf(publicKey))
//    }
//
//    fun encryptData(data: ByteArray, alias: String): ByteArray {
//        val keyStore = KeyStore.getInstance("AndroidKeyStore")
//        keyStore.load(null)
//        val publicKey = keyStore.getCertificate(alias).publicKey as PublicKey
//
//        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
//
//        return cipher.doFinal(data)
//    }
//
//    fun decryptData(encryptedData: ByteArray, alias: String): ByteArray {
//        val keyStore = KeyStore.getInstance("AndroidKeyStore")
//        keyStore.load(null)
//        val privateKey = keyStore.getKey(alias, null) as PrivateKey
//
//        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
//        cipher.init(Cipher.DECRYPT_MODE, privateKey)
//
//        return cipher.doFinal(encryptedData)
//    }

}


