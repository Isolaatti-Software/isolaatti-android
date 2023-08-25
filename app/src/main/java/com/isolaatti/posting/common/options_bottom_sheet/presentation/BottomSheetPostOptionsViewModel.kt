package com.isolaatti.posting.common.options_bottom_sheet.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isolaatti.posting.common.options_bottom_sheet.domain.OptionClicked
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options

class BottomSheetPostOptionsViewModel : ViewModel() {
    private val _options: MutableLiveData<Options> = MutableLiveData()
    val options: LiveData<Options> get() = _options

    private var _callerId: Int = 0

    private val _optionClicked: MutableLiveData<OptionClicked?> = MutableLiveData()
    val optionClicked: LiveData<OptionClicked?> get() = _optionClicked

    private var _payload: Any? = null

    fun handle() {
        _optionClicked.postValue(null)
    }

    fun setOptions(options: Options, callerId: Int, payload: Any? = null) {
        _options.postValue(options)
        _callerId = callerId
        _payload = payload

    }

    fun optionClicked(optionId: Int) {
        _optionClicked.postValue(OptionClicked(optionId, _callerId, _payload))
    }

}