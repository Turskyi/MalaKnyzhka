package com.turskyi.malaknyzhka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.turskyi.malaknyzhka.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(
                settings = remember {
                    createSettings(applicationContext)
                }
            )
        }
    }
}