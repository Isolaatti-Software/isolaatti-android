package com.isolaatti.posting.common.options_bottom_sheet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.isolaatti.R
import com.isolaatti.databinding.BottomSheetPostOptionsBinding
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options
import com.isolaatti.posting.common.options_bottom_sheet.presentation.BottomSheetPostOptionsViewModel

class BottomSheetPostOptionsFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: BottomSheetPostOptionsBinding

    private val viewModel: BottomSheetPostOptionsViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = BottomSheetPostOptionsBinding.inflate(inflater)


        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.options.observe(viewLifecycleOwner) {
            renderOptions(it)
        }
    }

    private fun renderOptions(options: Options) {
        viewBinding.optionsContainer.removeAllViews()
        for(option in options.items) {
            val button = MaterialButton(requireContext(), null, com.google.android.material.R.style.Widget_Material3_Button_TextButton)
            button.icon = AppCompatResources.getDrawable(requireContext(), option.icon)
            button.text = requireContext().getText(option.stringRes)
            button.textAlignment = MaterialButton.TEXT_ALIGNMENT_TEXT_START
            viewBinding.optionsContainer.addView(button)
        }
    }

    companion object {
        const val TAG = "BottomSheetPostOptions"


    }
}