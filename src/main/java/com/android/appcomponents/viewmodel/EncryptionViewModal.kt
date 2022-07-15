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

    fun aesEncryptAlgorithm(password: String, plaintext: String) {

        _EncryptResponse.postValue(aesEncrypt.encrypt(password, plaintext))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun aesDecryptionAlgorithm(password: String, plaintext: String) {

        _decryptResponse.postValue(aesEncrypt.decrypt(password, plaintext))
    }

    fun rsaEncryptAlgorithm(plaintext: String,publicKey : PublicKey) {

        _rsaEncryptResponse.postValue(encryptionUtility.rsaEncrption(plaintext, publicKey))
    }
    fun rsaDecryptAlgorithm(plaintext: String,privateKey: PrivateKey ) {

        _rsaDecryptResponse.postValue(encryptionUtility.rsaDecryption(plaintext, privateKey))
    }

    fun hMacSha256Algoritham(key: String, data: String): MutableLiveData<EncryptionData> {

        return encryptionUtility.hMacSha256Algoritham(key, data);
    }

    fun generateSecreteKey(biteValue: Int): MutableLiveData<SecreteKeyData>? {
        return encryptionUtility.generateSecreteKey(biteValue)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64Encode(plainText: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.toBase64Encode(plainText)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64Dncode(encodedString: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.toBase64Decode(encodedString)
    }

    fun md5Digest(plainText: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.md5Digest(plainText)
    }

    fun md5Filechecksum(filePath: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.md5Filechecksum(filePath)
    }

}