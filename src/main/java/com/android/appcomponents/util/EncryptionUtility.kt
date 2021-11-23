package com.android.appcomponents.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.android.appcomponents.model.EncryptionData
import com.android.appcomponents.model.SecreteKeyData
import java.io.FileInputStream
import java.io.IOException
import java.lang.IllegalArgumentException
import java.security.DigestInputStream
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object EncryptionUtility {


    fun encryptAesAlgoritham(plaintext: String, key: SecretKey?): MutableLiveData<EncryptionData> {
        val encryptionData = MutableLiveData<EncryptionData>()
        val cipherText = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(key?.encoded, "AES")
        cipherText.init(Cipher.ENCRYPT_MODE, keySpec)
        cipherText.doFinal(plaintext.toByteArray())
        val encriptedData = EncryptionData(cipherText.toString());
        encryptionData.postValue(encriptedData)
        return encryptionData

    }

    fun decryptAesAlgoritham(
        cipherText: ByteArray?,
        key: SecretKey
    ): MutableLiveData<EncryptionData>? {
        val decryptionData = MutableLiveData<EncryptionData>()
        try {
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(key.encoded, "AES")
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val decryptedText = cipher.doFinal(cipherText)
            val decryptedData = EncryptionData(decryptedText.toString());
            decryptionData.postValue(decryptedData)
            return decryptionData
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun hMacSha256Algoritham(key: String, data: String): MutableLiveData<EncryptionData> {
        val encriptionData = MutableLiveData<EncryptionData>()
        var returnString: String = ""
        try {
            val secretKey = SecretKeySpec(key.toByteArray(), "HmacSHA256")
            val mac = Mac.getInstance(secretKey.algorithm)
            mac.init(secretKey)
            val hMac = mac.doFinal(data.toByteArray())
            returnString = toHexString(hMac)
        } catch (ignore: NoSuchAlgorithmException) {

        } catch (ignore: InvalidKeyException) {

        }
        val encriptedData = EncryptionData(returnString);
        encriptionData.postValue(encriptedData)
        return encriptionData
    }

    private fun toHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        val formatter = Formatter(sb)
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return sb.toString()
    }

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
        val secreteKeyValue = SecreteKeyData(secretKey);
        secretKeyData.postValue(secreteKeyValue)
        return secretKeyData
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64Encode(plainText: String): MutableLiveData<EncryptionData> {
        val encriptionData = MutableLiveData<EncryptionData>()
        val encoder: Base64.Encoder = Base64.getEncoder()
        val encodedBytes: ByteArray = encoder.encode(plainText.toByteArray())
        val encriptedData = EncryptionData(String(encodedBytes));
        encriptionData.postValue(encriptedData)
        return encriptionData
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64Decode(encodedString: String): MutableLiveData<EncryptionData> {
        val decreptionData = MutableLiveData<EncryptionData>()
        val decoder: Base64.Decoder = Base64.getDecoder()
        val decodedBytes: ByteArray = decoder.decode(encodedString)
        val encriptedData = EncryptionData(String(decodedBytes));
        decreptionData.postValue(encriptedData)
        return decreptionData;
    }

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
                    while (dis.read() !== -1);
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

}