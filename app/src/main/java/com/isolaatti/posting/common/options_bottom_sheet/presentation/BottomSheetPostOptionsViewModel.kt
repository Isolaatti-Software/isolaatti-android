package com.isolaatti.posting.common.options_bottom_sheet.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.posting.common.domain.Ownable
import com.isolaatti.posting.common.options_bottom_sheet.domain.OptionClicked
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options.Companion.POST_OPTIONS
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.settings.domain.UserIdSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetPostOptionsViewModel @Inject constructor(private val userIdSetting: UserIdSetting) : ViewModel() {
    private val _options: MutableLiveData<Options> = MutableLiveData()
    val options: LiveData<Options> get() = _options

    private var _callerId: Int = 0

    private val _optionClicked: MutableLiveData<OptionClicked?> = MutableLiveData()
    val optionClicked: LiveData<OptionClicked?> get() = _optionClicked

    private var _payload: Any? = null

    fun handle() {
        _optionClicked.postValue(null)
    }

    fun setOptions(options: Int, callerId: Int, payload: Ownable? = null) {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                when(options) {
                    POST_OPTIONS -> {
                        userIdSetting.getUserId()?.let { userId ->
                            if(userId == payload?.userId) {
                                _options.postValue(Options.myPostOptions)
                            } else {
                                _options.postValue(Options.postOptions)
                            }
                            _callerId = callerId
                            _payload = payload
                        }

                    }
                }
            }

        }



    }

    fun optionClicked(optionId: Int) {
        _optionClicked.postValue(OptionClicked(optionId, _callerId, _payload))
    }

}