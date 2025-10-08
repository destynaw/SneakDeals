package com.example.sneakdeals.ui.screens.akun

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.ui.navigation.Screen
import com.example.sneakdeals.ui.theme.SneakDealsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Akun Saya", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            item {
                LoginPromptSection(navController)
                Divider(color = Color.Gray.copy(alpha = 0.2f), thickness = 8.dp)
            }
            item {
                AccountOption(text = "Benefit Level Kamu") { /* TODO */ }
            }

            item {
                SectionHeader("LAINNYA")
            }
            
            item { AccountOption(text = "Tentang Kami") { /* TODO */ } }
            item { AccountOption(text = "Syarat & Ketentuan") { /* TODO */ } }
            item { AccountOption(text = "Kebijakan Privasi") { /* TODO */ } }
            item { AccountOption(text = "Pusat Bantuan") { /* TODO */ } }
            item { AccountOption(text = "Panduan Ukuran") { /* TODO */ } }
            item { AccountOption(text = "Lokasi Toko") { navController.navigate(Screen.Toko.route) } }

            item {
                Divider(color = Color.Gray.copy(alpha = 0.2f), thickness = 8.dp)
            }

            item {
                AccountOption(text = "Push Notifikasi") { /* TODO */ }
            }
        }
    }
}

@Composable
fun LoginPromptSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Login/Daftar untuk menikmati keuntungan penuh dari SneakDeals",
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = { navController.navigate(Screen.Login.route) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Login atau Daftar", modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun AccountOption(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.Gray.copy(alpha = 0.2f))
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    SneakDealsTheme {
        AccountScreen(navController = rememberNavController())
    }
}
