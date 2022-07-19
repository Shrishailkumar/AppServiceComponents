package com.android.appcomponents.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.android.appcomponents.model.EncryptionData
import com.android.appcomponents.model.SecreteKeyData
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object EncryptionUtility {



    fun aesEncryptAlgorithm(plaintext: ByteArray?,key: SecretKey,IV: ByteArray?): MutableLiveData<EncryptionData> {
        val encryptionData = MutableLiveData<EncryptionData>()
        val cipher = Cipher.getInstance("AES")
        val keySpec =SecretKeySpec(key.encoded, "AES")
        val ivSpec = IvParameterSpec(IV)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val cipherText = cipher.doFinal(plaintext)
        val encriptedData = EncryptionData(cipherText.toString())
        encryptionData.postValue(encriptedData)
        return encryptionData
    }

    fun aesDecrytAlgorithm(cipherText: ByteArray?, key: SecretKey, IV: ByteArray?): MutableLiveData<EncryptionData> {
            val encryptionData = MutableLiveData<EncryptionData>()
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(key.encoded, "AES")
            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

            val decryptedText = cipher.doFinal(stringToBytes(cipherText.toString()))
            val encriptedData = EncryptionData(decryptedText.toString())
            encryptionData.postValue(encriptedData)
            return encryptionData

    }
    /**
     * Method for hMacSha256Algoritham
     */

    fun hMacSha256Algoritham(key: String, data: String): MutableLiveData<EncryptionData> {
        val encriptionData = MutableLiveData<EncryptionData>()
        var returnString = ""
        try {
            val secretKey = SecretKeySpec(key.toByteArray(), "HmacSHA256")
            val mac = Mac.getInstance(secretKey.algorithm)
            mac.init(secretKey)
            val hMac = mac.doFinal(data.toByteArray())
            returnString = toHexString(hMac)
        } catch (ignore: NoSuchAlgorithmException) {

        } catch (ignore: InvalidKeyException) {

        }
        val encriptedData = EncryptionData(returnString)
        encriptionData.postValue(encriptedData)
        return encriptionData
    }
    /**
     * Method to convert bytearray to string
     */
    private fun toHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        val formatter = Formatter(sb)
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return sb.toString()
    }
    /**
     * Method to generate secrete key
     */
    fun generateSecreteKey(bitValue: Int): MutableLiveData<SecreteKeyData>? {
        val secretKeyData = MutableLiveData<SecreteKeyData>()
        val keyGenerator: KeyGenerator
        var secretKey: SecretKey? = null
        try {
            keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(bitValue)
            secretKey = keyGenerator.generateKey()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val secreteKeyValue = SecreteKeyData(secretKey)
        secretKeyData.postValue(secreteKeyValue)
        return secretKeyData
    }
    /**
     * Method to encode using base64 algorithm
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64Encode(plainText: String): MutableLiveData<EncryptionData> {
        val encriptionData = MutableLiveData<EncryptionData>()
        val encoder: Base64.Encoder = Base64.getEncoder()
        val encodedBytes: ByteArray = encoder.encode(plainText.toByteArray())
        val encriptedData = EncryptionData(String(encodedBytes))
        encriptionData.postValue(encriptedData)
        return encriptionData
    }
    /**
     * Method to decode using base64 algorithm
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64Decode(encodedString: String): MutableLiveData<EncryptionData> {
        val decreptionData = MutableLiveData<EncryptionData>()
        val decoder: Base64.Decoder = Base64.getDecoder()
        val decodedBytes: ByteArray = decoder.decode(encodedString)
        val encriptedData = EncryptionData(String(decodedBytes))
        decreptionData.postValue(encriptedData)
        return decreptionData
    }
    /**
     * Method for md5Digest
     */
    fun md5Digest(input: String): MutableLiveData<EncryptionData> {
        val encryptionData = MutableLiveData<EncryptionData>()
        val md: MessageDigest
        md = try {
            MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException(e)
        }
        val encriptedData = EncryptionData(String(md.digest(input.toByteArray())))
        encryptionData.postValue(encriptedData)
        return encryptionData
    }

    /**
     * Method for md5Filechecksum
     */
     fun md5Filechecksum(filePath: String): MutableLiveData<EncryptionData> {
         val encryptionData = MutableLiveData<EncryptionData>()
        var md: MessageDigest
        md = try {
            MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException(e)
        }
        try {
            FileInputStream(filePath).use { `is` ->
                DigestInputStream(`is`, md).use { dis ->
                    while (dis.read() !== -1)
                    md = dis.getMessageDigest()
                }
            }
        } catch (e: IOException) {
            throw IllegalArgumentException(e)
        }
         val encriptedData = EncryptionData(String(md.digest()))
         encryptionData.postValue(encriptedData)
        return encryptionData
    }
    /**
     * Method to encrypt using RSA algorithm
     * @return String
     */

    fun rsaEncrption(plain: String,publicKey :PublicKey): String? {
        val cipher: Cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(plain.toByteArray())
        val encrypted = bytesToString(encryptedBytes)
        return encrypted
    }

    /**
     * Method to Decrypt using RSA algorithm
     * @return String
     */
    fun rsaDecryption(result: String,privateKey: PrivateKey): String? {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedBytes = cipher.doFinal(stringToBytes(result))
        val decrypted = String(decryptedBytes)
        return decrypted
    }
    /**
     * Method to comvert byte to string
     * @return String
     */
    fun bytesToString(b: ByteArray): String? {
        val b2 = ByteArray(b.size + 1)
        b2[0] = 1
        System.arraycopy(b, 0, b2, 1, b.size)
        return BigInteger(b2).toString(36)
    }
    /**
     * Method to convert string to byte
     * @return ByteArray
     */
    fun stringToBytes(s: String?): ByteArray? {
        val b2: ByteArray = BigInteger(s, 36).toByteArray()
        return Arrays.copyOfRange(b2, 1, b2.size)
    }
}