package com.isolaatti.audio.drafts.domain

import android.net.Uri
import androidx.core.net.toUri
import com.isolaatti.MyApplication
import com.isolaatti.audio.common.domain.Playable
import java.io.File

data class AudioDraft(val id: Long, val name: String, val localStorageRelativePath: String) : Playable() {
    override val thumbnail: String?
        get() = null

    override val uri: Uri
        get() {
            val appFiles = MyApplication.myApp.applicationContext.filesDir
            return File(appFiles, localStorageRelativePath).toUri()
        }
}