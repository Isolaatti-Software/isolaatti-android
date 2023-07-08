package com.isolaatti.posting.common.options_bottom_sheet.domain

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.isolaatti.R

data class Options(
    @StringRes val title: Int,
    val items: List<Option>
) {
    data class Option(
        @StringRes val stringRes: Int,
        @DrawableRes val icon: Int
    )

    companion object {
        val postOptions = Options(R.string.post_options_title, listOf(
            Option(R.string.delete, R.drawable.baseline_delete_24),
            Option(R.string.edit, R.drawable.baseline_edit_24),
            Option(R.string.report, R.drawable.baseline_report_24)
        ))

        val commentOptions = Options(R.string.post_options_title, listOf(
            Option(R.string.delete, R.drawable.baseline_delete_24),
            Option(R.string.edit, R.drawable.baseline_edit_24),
            Option(R.string.report, R.drawable.baseline_report_24)
        ))
    }
}