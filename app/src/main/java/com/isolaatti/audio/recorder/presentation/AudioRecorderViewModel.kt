package com.isolaatti.audio.recorder.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.audio.drafts.domain.AudioDraft
import com.isolaatti.audio.drafts.domain.use_case.SaveAudioDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioRecorderViewModel @Inject constructor(
    private val saveAudioDraftUC: SaveAudioDraft
) : ViewModel() {
    var name: String = ""
        set(value) {
            field = value
            validate()
        }
    var relativePath = ""
    private var audioRecorded = false

    val audioDraft: MutableLiveData<AudioDraft> = MutableLiveData()

    val canSave: MutableLiveData<Boolean> = MutableLiveData()

    private fun validate() {
        canSave.value = audioRecorded && name.isNotBlank()
    }

    fun onAudioRecorded() {
        audioRecorded = true
        validate()
    }

    fun onClearAudio() {
        audioRecorded = false
        validate()
    }
    fun saveAudioDraft() {
        viewModelScope.launch {
            saveAudioDraftUC(name, relativePath).onEach {
                audioDraft.postValue(it)
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}