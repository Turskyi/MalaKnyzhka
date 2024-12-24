package com.turskyi.malaknyzhka

import android.content.Context
import android.content.SharedPreferences
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

fun createSettings(context: Context): Settings {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "MalaKnyzhkaPreferences",
        Context.MODE_PRIVATE,
    )
    return SharedPreferencesSettings(sharedPreferences)
}