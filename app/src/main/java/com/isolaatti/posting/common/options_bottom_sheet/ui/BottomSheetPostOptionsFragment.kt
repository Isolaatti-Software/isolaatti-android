package com.isolaatti.posting.common.options_bottom_sheet.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.isolaatti.R
import com.isolaatti.databinding.BottomSheetPostOptionsBinding
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options
import com.isolaatti.posting.common.options_bottom_sheet.presentation.BottomSheetPostOptionsViewModel
import com.isolaatti.posting.common.options_bottom_sheet.presentation.OptionsRecyclerAdapter

class BottomSheetPostOptionsFragment : BottomSheetDialogFragment(), OptionsRecyclerAdapter.OptionsCallback {
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
            Log.d("BottomSheetPostOptionsFragment", "entra")
            renderOptions(it)
        }

        viewModel.optionClicked.observe(viewLifecycleOwner) {
            if(it != null) {
                (dialog as BottomSheetDialog).dismiss()
            }


        }
    }


    private fun renderOptions(options: Options) {
        viewBinding.recyclerOptions.adapter = OptionsRecyclerAdapter(options.items, this)
        viewBinding.recyclerOptions.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        const val TAG = "BottomSheetPostOptions"


    }

    override fun optionClicked(optionId: Int) {
        viewModel.optionClicked(optionId)
    }
}