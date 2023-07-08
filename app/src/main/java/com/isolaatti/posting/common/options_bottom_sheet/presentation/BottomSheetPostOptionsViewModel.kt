package com.isolaatti.posting.common.options_bottom_sheet.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options

class BottomSheetPostOptionsViewModel : ViewModel() {
    private val _options: MutableLiveData<Options> = MutableLiveData()
    val options: LiveData<Options> get() = _options

    fun setOptions(options: Options) {
        _options.postValue(options)
    }
}