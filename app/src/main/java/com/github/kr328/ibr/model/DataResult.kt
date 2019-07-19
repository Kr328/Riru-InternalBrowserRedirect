package com.github.kr328.ibr.model

data class DataResult<T>(val status: Int, val result: T) {
    companion object {
        const val STATUS_SUCCESS = 0
        const val STATUS_UNKNOWN = -1
    }
}