package com.example.sneakdeals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.ui.navigation.Screen
import com.example.sneakdeals.ui.screens.MainAppScreen
// import com.example.sneakdeals.ui.screens.login.LoginScreen // <-- Sudah tidak diperlukan
import com.example.sneakdeals.ui.theme.SneakDealsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SneakDealsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SneakDealsApp()
                }
            }
        }
    }
}

@Composable
fun SneakDealsApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.MainApp.route // <-- SAYA UBAH INI
    ) {
        // Blok untuk LoginScreen sudah saya hapus
        composable(Screen.MainApp.route) {
            MainAppScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SneakDealsTheme {
        SneakDealsApp()
    }
}
