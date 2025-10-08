package com.example.sneakdeals.ui.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sneakdeals.data.model.cartItems
import com.example.sneakdeals.ui.screens.kategori.ProductListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    val total by remember { derivedStateOf { cartItems.sumOf { it.price } } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Keranjang Belanja", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                CartSummary(total = total, onCheckoutClick = { /* TODO */ })
            }
        }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Keranjang Anda masih kosong.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cartItems) { product ->
                    ProductListItem(product = product, onClick = { /* Navigate to detail */ })
                }
            }
        }
    }
}

@Composable
fun CartSummary(total: Double, onCheckoutClick: () -> Unit) {
    Surface(shadowElevation = 8.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Estimasi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("Rp${String.format("%,.0f", total)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onCheckoutClick,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Lanjut ke Checkout")
            }
        }
    }
}
