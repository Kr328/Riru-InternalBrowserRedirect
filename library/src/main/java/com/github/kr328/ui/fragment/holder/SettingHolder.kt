package com.github.kr328.ui.fragment.holder

interface SettingHolder {
    val id: String
    val type: Type

    enum class Type {
        APP_INFO,
        TITLE,
        CLIAKABLE_ITEM
    }
}