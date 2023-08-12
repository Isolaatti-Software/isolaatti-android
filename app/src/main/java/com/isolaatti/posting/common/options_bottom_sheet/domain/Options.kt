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
        @DrawableRes val icon: Int,
        val optionId: Int
    ) {
        companion object {
            const val OPTION_DELETE = 1
            const val OPTION_EDIT = 2
            const val OPTION_REPORT = 3
        }
    }

    companion object {
        val myPostOptions = Options(R.string.post_options_title, listOf(
            Option(R.string.delete, R.drawable.baseline_delete_24, Option.OPTION_DELETE),
            Option(R.string.edit, R.drawable.baseline_edit_24, Option.OPTION_EDIT),
            Option(R.string.report, R.drawable.baseline_report_24, Option.OPTION_REPORT)
        ))

        val postOptions = Options(R.string.post_options_title, listOf(
            Option(R.string.report, R.drawable.baseline_report_24, Option.OPTION_REPORT)
        ))

        val myCommentOptions = Options(R.string.post_options_title, listOf(
            Option(R.string.delete, R.drawable.baseline_delete_24, Option.OPTION_DELETE),
            Option(R.string.edit, R.drawable.baseline_edit_24, Option.OPTION_EDIT),
            Option(R.string.report, R.drawable.baseline_report_24, Option.OPTION_REPORT)
        ))

        val commentOptions = Options(R.string.post_options_title, listOf(
            Option(R.string.report, R.drawable.baseline_report_24, Option.OPTION_REPORT)
        ))
    }
}