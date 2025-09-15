package com.mahesh.demo.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.mahesh.demo.presentation.navigation.DemoNavGraph
import com.mahesh.demo.presentation.theme.Mahesh_Ambekar_DemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()

        setContent {
            Mahesh_Ambekar_DemoTheme {
                DemoNavGraph()
            }
        }
    }
}