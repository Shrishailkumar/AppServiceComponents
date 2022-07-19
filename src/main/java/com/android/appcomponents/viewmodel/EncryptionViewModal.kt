package com.android.appcomponents.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.appcomponents.model.EncryptionData
import com.android.appcomponents.model.SecreteKeyData
import com.android.appcomponents.util.EncryptionUtility
import com.zensar.aes.AESCrypt
import java.security.PrivateKey
import java.security.PublicKey

class EncryptionViewModal : ViewModel() {
    val encryptionUtility = EncryptionUtility
    val aesEncrypt = AESCrypt
    val _EncryptResponse = MutableLiveData<String>()
    val encryptedResponse: LiveData<String> = _EncryptResponse

    val _decryptResponse = MutableLiveData<String>()
    val decryptedResponse: LiveData<String> = _decryptResponse

    val _rsaEncryptResponse = MutableLiveData<String>()
    val rsaEncryptResponse: LiveData<String> = _rsaEncryptResponse

    val _rsaDecryptResponse = MutableLiveData<String>()
    val rsaDecryptResponse: LiveData<String> = _rsaDecryptResponse

    /**
     * Method to encrypt using aesalgorithm
     * @return String (encrypted string)
     */
    fun aesEncryptAlgorithm(password: String, plaintext: String) {

        _EncryptResponse.postValue(aesEncrypt.encrypt(password, plaintext))
    }
    /**
     * Method to decrypt using aesalgorithm
     * @return String (decrypt string)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun aesDecryptionAlgorithm(password: String, plaintext: String) {

        _decryptResponse.postValue(aesEncrypt.decrypt(password, plaintext))
    }
    /**
     * Method to encrypt using rsa algorithm
     * @return String (encrypt string)
     */
    fun rsaEncryptAlgorithm(plaintext: String,publicKey : PublicKey) {

        _rsaEncryptResponse.postValue(encryptionUtility.rsaEncrption(plaintext, publicKey))
    }
    /**
     * Method to decrypt using rsa algorithm
     * @return String (decrypt string)
     */
    fun rsaDecryptAlgorithm(plaintext: String,privateKey: PrivateKey ) {

        _rsaDecryptResponse.postValue(encryptionUtility.rsaDecryption(plaintext, privateKey))
    }
    /**
     * Method to encrypt using hmac256algorithm
     * @return livedata (MutableLiveData<EncryptionData>)
     */
    fun hMacSha256Algoritham(key: String, data: String): MutableLiveData<EncryptionData> {

        return encryptionUtility.hMacSha256Algoritham(key, data);
    }
    /**
     * Method to generate secrete key
     * @return livedata (MutableLiveData<SecreteKeyData>)
     */
    fun generateSecreteKey(biteValue: Int): MutableLiveData<SecreteKeyData>? {
        return encryptionUtility.generateSecreteKey(biteValue)
    }

    /**
     * Method to encrypt using base64 algorithm
     * @return livedata (MutableLiveData<EncryptionData>)
     */

    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64Encode(plainText: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.toBase64Encode(plainText)
    }
    /**
     * Method to decrypt using base64 algorithm
     * @return livedata (MutableLiveData<EncryptionData>)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64Dncode(encodedString: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.toBase64Decode(encodedString)
    }
    /**
     * Method to md5Digest
     * @return livedata (MutableLiveData<EncryptionData>)
     */
    fun md5Digest(plainText: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.md5Digest(plainText)
    }
    /**
     * Method to md5Filechecksum
     * @return livedata (MutableLiveData<EncryptionData>)
     */
    fun md5Filechecksum(filePath: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.md5Filechecksum(filePath)
    }

}