package com.horizon.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.horizon.app.navigation.HorizonNavGraph
import com.horizon.app.ui.AppViewModel
import com.horizon.app.ui.theme.HorizonTheme

class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HorizonTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HorizonNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
