package com.example.sneakdeals.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sneakdeals.ui.theme.SneakDealsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (email: String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Login Pengguna") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Selamat Datang Kembali!", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email atau Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        onLoginSuccess(email)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("LOGIN")
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("Belum Punya Akun? Daftar di Sini")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SneakDealsTheme {
        LoginScreen(onLoginSuccess = {}, onNavigateToRegister = {})
    }
}
