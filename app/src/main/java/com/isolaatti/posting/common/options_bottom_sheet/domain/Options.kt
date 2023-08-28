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
            const val OPTION_SAVE = 4
            const val OPTION_SNAPSHOT = 5
        }
    }

    companion object {
        const val POST_OPTIONS = 1
        const val COMMENT_OPTIONS = 2

        val noOptions = Options(0, listOf())

        fun getPostsOptions(userOwned: Boolean, savable: Boolean, snapshotAble: Boolean): Options {
            val list = mutableListOf(
                Option(R.string.report, R.drawable.baseline_report_24, Option.OPTION_REPORT)
            )

            if(userOwned) {
                list.addAll(listOf(
                    Option(R.string.delete, R.drawable.baseline_delete_24, Option.OPTION_DELETE),
                    Option(R.string.edit, R.drawable.baseline_edit_24, Option.OPTION_EDIT))
                )
            }

            if(savable) {
                list.add(Option(R.string.save, R.drawable.baseline_save_24, Option.OPTION_SAVE))
            }

            if(snapshotAble) {
                list.add(Option(R.string.save_snapshot, R.drawable.baseline_download_24, Option.OPTION_SNAPSHOT))
            }

            return Options(R.string.post_options_title, list)
        }

        fun getCommentOptions(userOwned: Boolean, savable: Boolean, snapshotAble: Boolean): Options {
            val list = mutableListOf(
                Option(R.string.report, R.drawable.baseline_report_24, Option.OPTION_REPORT)
            )

            if(userOwned) {
                list.addAll(listOf(
                    Option(R.string.delete, R.drawable.baseline_delete_24, Option.OPTION_DELETE),
                    Option(R.string.edit, R.drawable.baseline_edit_24, Option.OPTION_EDIT))
                )
            }

            if(savable) {
                list.add(Option(R.string.save, R.drawable.baseline_save_24, Option.OPTION_SAVE))
            }

            if(snapshotAble) {
                list.add(Option(R.string.save_snapshot, R.drawable.baseline_download_24, Option.OPTION_SNAPSHOT))
            }

            return Options(R.string.comment_options, list)
        }
    }
}