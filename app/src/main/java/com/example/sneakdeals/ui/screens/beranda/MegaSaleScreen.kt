package com.example.sneakdeals.ui.screens.beranda

import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.ui.screens.kategori.ProductListItem
import com.example.sneakdeals.ui.screens.kategori.allProducts
import com.example.sneakdeals.ui.theme.SneakDealsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MegaSaleScreen(navController: NavController) {
    // Filter for only discounted products
    val discountedProducts = allProducts.filter { it.originalPrice != null }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mega Sale Beli 1 Gratis 1", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                CountdownTimerSection()
                Spacer(Modifier.height(24.dp))
            }

            item {
                TermsAndConditionsSection()
                Spacer(Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Semua Produk Diskon",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(16.dp))
            }

            items(discountedProducts) { product ->
                 Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    ProductListItem(product = product, onClick = { /* Navigate to product detail */ })
                }
            }
        }
    }
}

@Composable
fun CountdownTimerSection() {
    // Set a fixed sale end time for demonstration (e.g., 3 days from now)
    val saleEndTime = remember { System.currentTimeMillis() + TimeUnit.DAYS.toMillis(3) + TimeUnit.HOURS.toMillis(7) }
    var timeLeft by remember { mutableStateOf(saleEndTime - System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft = saleEndTime - System.currentTimeMillis()
        }
    }

    val days = TimeUnit.MILLISECONDS.toDays(timeLeft)
    val hours = TimeUnit.MILLISECONDS.toHours(timeLeft) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("Berakhir dalam:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TimerBox(value = days.toString().padStart(2, '0'), label = "Hari")
            TimerBox(value = hours.toString().padStart(2, '0'), label = "Jam")
            TimerBox(value = minutes.toString().padStart(2, '0'), label = "Menit")
            TimerBox(value = seconds.toString().padStart(2, '0'), label = "Detik")
        }
    }
}

@Composable
fun RowScope.TimerBox(value: String, label: String) {
    Box(
        modifier = Modifier
            .weight(1f)
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

@Composable
fun TermsAndConditionsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("Syarat & Ketentuan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(Icons.Default.Info, contentDescription = "Info S&K", tint = Color.Gray)
            Spacer(Modifier.width(16.dp))
            Text(
                text = "1. Promo Beli 1 Gratis 1 berlaku untuk semua produk dengan label diskon.\n" +
                       "2. Item gratis adalah item dengan harga terendah.\n" +
                       "3. Promo tidak dapat digabungkan dengan voucher lain.\n" +
                       "4. Persediaan terbatas dan promo dapat berakhir sewaktu-waktu.",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MegaSaleScreenPreview() {
    SneakDealsTheme {
        MegaSaleScreen(navController = rememberNavController())
    }
}
