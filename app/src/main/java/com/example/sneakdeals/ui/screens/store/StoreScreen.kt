package com.example.sneakdeals.ui.screens.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.data.model.Store
import com.example.sneakdeals.data.model.allStores
import com.example.sneakdeals.ui.navigation.Screen
import com.example.sneakdeals.ui.theme.SneakDealsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lokasi Toko Puma") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Daftar Toko & Promosi Terdekat:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            items(allStores) { store ->
                StoreCard(store = store, navController = navController)
            }
        }
    }
}

@Composable
fun StoreCard(store: Store, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Screen.StoreDetail.createRoute(store.id)) }, // <-- AKSI KLIK
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = store.imageRes),
                contentDescription = "Gambar ${store.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(16.dp)) {
                Text(store.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(store.address, style = MaterialTheme.typography.bodyMedium)
                Text(store.city, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text(store.hours, style = MaterialTheme.typography.bodyMedium)
                Text(store.phone, style = MaterialTheme.typography.bodyMedium)
                if (store.promotions.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text("Promosi:", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    store.promotions.forEach { promo ->
                        Text("- $promo", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreScreenPreview() {
    SneakDealsTheme {
        StoreScreen(navController = rememberNavController())
    }
}
