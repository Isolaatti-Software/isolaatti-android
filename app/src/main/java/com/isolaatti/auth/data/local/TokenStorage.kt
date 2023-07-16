package com.isolaatti.auth.data.local

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.isolaatti.auth.data.remote.AuthTokenDto
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import java.security.GeneralSecurityException
import javax.inject.Inject


class TokenStorage @Inject constructor(@ApplicationContext private val application: Context) {
    var token: AuthTokenDto? = null

    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    private val fileName = "token.isolaatti"

    private val file = File(application.filesDir, fileName)
    private val encryptedFile = EncryptedFile.Builder(
        file,
        application,
        mainKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()


    init {
        token = getSavedToken()
    }

    private val gson get() = Gson()

    private fun getSavedToken(): AuthTokenDto? {
        try {


            val jsonEncodedInputStream = encryptedFile.openFileInput()
            val jsonEncodedBytes = jsonEncodedInputStream.readBytes()
            jsonEncodedInputStream.close()
            return gson.fromJson(String(jsonEncodedBytes), AuthTokenDto::class.java)
        } catch(e: IllegalArgumentException) {
            return null
        } catch(e: JsonSyntaxException) {
            return null
        } catch (e: IOException) {
            return null
        } catch(e: GeneralSecurityException) {
            return null
        }
    }

    fun storeToken(token: AuthTokenDto): Boolean {
        try {
            val jsonEncodedBytes = gson.toJson(token).toByteArray(Charsets.UTF_8)

            if(file.exists()) {
                file.delete()
            }

            encryptedFile.openFileOutput().apply {
                write(jsonEncodedBytes)
                flush()
                close()
            }

            // Keeps this token in memory
            this.token = token

            return true
        } catch(e: IOException) {
            return false
        }
    }

    fun removeToken() {
        if(file.exists()) {
            try {
                file.delete()
            } catch(_: SecurityException) { }
        }
    }
}