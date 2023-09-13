package com.isolaatti.common.options_bottom_sheet.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.common.Ownable
import com.isolaatti.common.options_bottom_sheet.domain.OptionClicked
import com.isolaatti.common.options_bottom_sheet.domain.Options
import com.isolaatti.common.options_bottom_sheet.domain.Options.Companion.COMMENT_OPTIONS
import com.isolaatti.common.options_bottom_sheet.domain.Options.Companion.POST_OPTIONS
import com.isolaatti.common.options_bottom_sheet.domain.Options.Companion.PROFILE_PHOTO_OPTIONS
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
                            _options.postValue(
                                Options.getPostsOptions(
                                userOwned = userId == payload?.userId,
                                savable = false,
                                snapshotAble = false)
                            )
                            _callerId = callerId
                            _payload = payload
                        }
                    }
                    COMMENT_OPTIONS -> {
                        userIdSetting.getUserId()?.let { userId ->
                            _options.postValue(
                                Options.getCommentOptions(
                                userOwned = userId == payload?.userId,
                                savable = false,
                                snapshotAble = false)
                            )
                            _callerId = callerId
                            _payload = payload
                        }
                    }
                    PROFILE_PHOTO_OPTIONS -> {
                        userIdSetting.getUserId()?.let { userId ->
                            _options.postValue(Options.getProfilePhotoOptions(userOwned = userId == payload?.userId,))
                            _callerId = callerId
                            _payload = payload
                        }
                    }
                }
            }

        }
    }

    fun optionClicked(optionsId: Int, optionId: Int) {
        _optionClicked.postValue(OptionClicked(optionsId, optionId, _callerId, _payload))
    }
}