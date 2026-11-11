package com.kostku.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kostku.app.ui.navigation.AppNavHost
import com.kostku.app.ui.theme.KostKuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KostKuTheme {
                AppNavHost()
            }
        }
    }
}
