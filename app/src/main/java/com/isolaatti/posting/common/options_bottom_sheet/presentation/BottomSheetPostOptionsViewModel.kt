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

    private val _optionClicked: MutableLiveData<OptionClicked> = MutableLiveData()
    val optionClicked: LiveData<OptionClicked> get() = _optionClicked

    fun setOptions(options: Options, callerId: Int) {
        _options.postValue(options)
        _callerId = callerId
    }

    fun optionClicked(optionId: Int) {
        _optionClicked.postValue(OptionClicked(optionId, _callerId))
    }

}