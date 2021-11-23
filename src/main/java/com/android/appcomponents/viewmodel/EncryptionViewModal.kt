package com.android.appcomponents.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.appcomponents.model.EncryptionData
import com.android.appcomponents.model.SecreteKeyData
import com.android.appcomponents.util.EncryptionUtility
import javax.crypto.SecretKey

class EncryptionViewModal : ViewModel() {
    val encryptionUtility = EncryptionUtility

    fun aes256Algoritham(strToEncrypt: String,secreteKey:SecretKey?): MutableLiveData<EncryptionData> {

        return encryptionUtility.encryptAesAlgoritham(strToEncrypt,secreteKey);
    }
    fun hMacSha256Algoritham(key: String, data: String): MutableLiveData<EncryptionData> {

        return encryptionUtility.hMacSha256Algoritham(key,data);
    }
    fun generateSecreteKey(biteValue: Int): MutableLiveData<SecreteKeyData>? {
        return encryptionUtility.generateSecreteKey(biteValue);
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64Encode(plainText: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.toBase64Encode(plainText);
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64Dncode(encodedString: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.toBase64Decode(encodedString);
    }
    fun md5Digest(plainText: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.md5Digest(plainText);
    }
    fun md5Filechecksum(filePath: String): MutableLiveData<EncryptionData> {
        return encryptionUtility.md5Filechecksum(filePath);
    }

}