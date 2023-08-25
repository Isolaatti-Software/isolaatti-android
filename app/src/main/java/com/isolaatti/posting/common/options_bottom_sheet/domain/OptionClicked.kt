package com.isolaatti.posting.common.options_bottom_sheet.domain

/**
 * @param optionId  Identify action
 * @param callerId Identify who started dialog
 * @param payload Data to identify on what item perform action
 */
data class OptionClicked(val optionId: Int, val callerId: Int, val payload: Any? = null)