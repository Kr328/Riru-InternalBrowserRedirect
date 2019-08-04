package com.github.kr328.ibr.data.state

data class RuleDataStateResult(val state: RuleDataState, val success: Boolean, val data: Map<String, Any>) {
    constructor(state: RuleDataState, success: Boolean, packageName: String) : this(state, success, mapOf("packageName" to packageName))

    val packageName: String? by data
}