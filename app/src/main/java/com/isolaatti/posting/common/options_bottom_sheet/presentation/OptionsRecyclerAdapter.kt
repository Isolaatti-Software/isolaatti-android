package com.isolaatti.posting.common.options_bottom_sheet.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.isolaatti.databinding.OptionItemBinding
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options

class OptionsRecyclerAdapter(val options: List<Options.Option>, private val optionCallback: OptionsCallback) : RecyclerView.Adapter<OptionsRecyclerAdapter.OptionViewHolder>() {

    inner class OptionViewHolder(val viewBinding: OptionItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    fun interface OptionsCallback {
        fun optionClicked(optionId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        return OptionViewHolder(
            OptionItemBinding.inflate(LayoutInflater.from(parent.context)).apply {
                root.layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }

    override fun getItemCount(): Int = options.count()

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.viewBinding.optionButton.apply {
            text = context.getText(options[position].stringRes)
            icon = AppCompatResources.getDrawable(context, options[position].icon)
            setOnClickListener {
                optionCallback.optionClicked(options[position].optionId)
            }
        }
    }
}