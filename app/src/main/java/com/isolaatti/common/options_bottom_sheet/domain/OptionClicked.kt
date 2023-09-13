package com.isolaatti.common.options_bottom_sheet.domain

/**
 * @param optionsId Identify what dialog it is
 * @param optionId  Identify action
 * @param callerId Identify who started dialog
 * @param payload Data to identify on what item perform action
 */
data class OptionClicked(val optionsId: Int, val optionId: Int, val callerId: Int, val payload: Any? = null)