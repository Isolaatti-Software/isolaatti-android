package com.isolaatti.audio.audios_list.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.audio.common.domain.Audio
import com.isolaatti.audio.common.domain.AudiosRepository
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudiosViewModel @Inject constructor(private val audiosRepository: AudiosRepository) : ViewModel() {
    val resource: MutableLiveData<Resource<List<Audio>>> = MutableLiveData()


    fun loadAudios(userId: Int) {
        viewModelScope.launch {
            audiosRepository.getAudiosOfUser(userId, null).onEach {
                resource.postValue(it)

            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

}