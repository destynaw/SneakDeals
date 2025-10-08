package com.example.sneakdeals.ui.screens.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.sneakdeals.ui.screens.kategori.*
import com.example.sneakdeals.ui.theme.SneakDealsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(store: Store, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSort by remember { mutableStateOf("Relevansi") }
    var showSortSheet by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val sortSheetState = rememberModalBottomSheetState()
    val filterSheetState = rememberModalBottomSheetState()
    var selectedGender by remember { mutableStateOf<String?>(null) }

    val filteredAndSortedProducts by remember(allProducts, selectedSort, selectedGender, searchQuery) {
        derivedStateOf {
            val searchFiltered = if (searchQuery.isNotBlank()) {
                allProducts.filter { it.name.contains(searchQuery, ignoreCase = true) }
            } else {
                allProducts
            }
            val genderFiltered = if (selectedGender != null) {
                searchFiltered.filter { it.gender == selectedGender || it.gender == "Unisex" }
            } else {
                searchFiltered
            }
            when (selectedSort) {
                "Termurah" -> genderFiltered.sortedBy { it.price }
                "Termahal" -> genderFiltered.sortedByDescending { it.price }
                else -> genderFiltered // "Relevansi"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(store.name, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(store.mall, style = MaterialTheme.typography.bodyMedium, color = Color.Gray, maxLines = 1)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Chat.createRoute(store.id)) },
                shape = CircleShape
            ) {
                Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Chat dengan Toko")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            item {
                Spacer(Modifier.height(16.dp))
                SearchBarSection(query = searchQuery, onQueryChange = { searchQuery = it })
                Spacer(Modifier.height(24.dp))
            }
            item {
                OverviewSection(store)
                Spacer(Modifier.height(24.dp))
            }
            stickyHeader {
                Column(modifier = Modifier.background(Color.White)) {
                     SortFilterControls(
                        onSortClick = { showSortSheet = true },
                        onFilterClick = { showFilterSheet = true }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            item {
                Text(
                    "Semua Produk", 
                    style = MaterialTheme.typography.titleLarge, 
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(16.dp))
            }
            items(filteredAndSortedProducts) { product ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)){
                    ProductListItem(product = product, onClick = { navController.navigate(Screen.ProductDetail.createRoute(product.id)) })
                }
            }
        }

        if (showSortSheet) {
            ModalBottomSheet(onDismissRequest = { showSortSheet = false }, sheetState = sortSheetState) {
                SortOptionsSheet(selectedOption = selectedSort, onOptionSelected = { selectedSort = it; showSortSheet = false })
            }
        }

        if (showFilterSheet) {
            ModalBottomSheet(onDismissRequest = { showFilterSheet = false }, sheetState = filterSheetState) {
                FilterOptionsSheet(selectedGender = selectedGender, onGenderSelected = { selectedGender = it; showFilterSheet = false })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarSection(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(50.dp),
        placeholder = { Text("Cari produk di toko ini...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun OverviewSection(store: Store) {
    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("OVERVIEW", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray)
            Spacer(Modifier.width(16.dp))
            Text(store.address, style = MaterialTheme.typography.bodyMedium)
        }
        Image(
            painter = painterResource(id = store.imageRes),
            contentDescription = "Foto ${store.name}",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StoreDetailScreenPreview() {
    SneakDealsTheme {
        StoreDetailScreen(store = allStores.first(), navController = rememberNavController())
    }
}
